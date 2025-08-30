package io.github.folgue02.sapphire.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.github.folgue02.sapphire.exception.SapphireException;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.filter.RouteFilter;
import io.github.folgue02.sapphire.router.RouteSpecification;
import io.github.folgue02.sapphire.router.Router;
import io.github.folgue02.sapphire.router.handler.RouteHandler;
import io.github.folgue02.sapphire.server.exception.ResponseWriteException;
import io.github.folgue02.sapphire.utils.HttpUtils;
import io.github.folgue02.sapphire.utils.RouteUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SunSapphireServer extends SapphireServer implements HttpHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public SunSapphireServer(Router router, InetSocketAddress address) {
		super(router, address);
	}

	@Override
	public void run() throws Exception {
		try {
			var server = HttpServer.create(this.address, 0);
			
			server.createContext("/", this);
	
			log.info("Starting server on {}", this.address);
			server.start();
			log.info("Server started on {}", this.address);
		} catch (Exception e) {
			log.error("Error while starting server.", e);
			throw e;
		}
	}

	@Override
	public void handle(HttpExchange exchange) {
		final RouteSpecification rSpec;
		final RouteHandler routeHandler;

		// --- Read data from request ---
		final HttpRequest request;
		final HttpResponse response = new HttpResponse();
		try {
			request = HttpUtils.requestFromExchange(exchange);
		} catch (Exception e) {
			log.error("Couldn't read from request.", e);
			return;
		}

		
		// --- Filter execution ---
		// If a filter corresponds to the giving request, it is executed.
		// If the filter decides to forward the request to a different handler, this one is 
		// executed, and whatever other handler that would have been chosen for the request is ignored.
		List<RouteFilter> matchedRouteFilters = this.router.findMatchedFilters(request);

		for (var filter : matchedRouteFilters) {

			boolean continueChain = runFilter(request, response, exchange, filter);
			if (!continueChain)
				return;
		}

		// --- Get handler ---
		// If no handler is mapped for this request, use the this.noMappingFoundHandler
		Optional<Map.Entry<RouteSpecification, RouteHandler>> handlerOpt = this.router.findMatchedRoute(request);
		if (handlerOpt.isEmpty()) {
			routeHandler = this.noMappingFoundHandler;
			rSpec = new RouteSpecification(request.method, request.requestUri.getPath());
		} else {
			rSpec = handlerOpt.get().getKey();
			routeHandler = handlerOpt.get().getValue();
		}
		
		request.pathParams = RouteUtils.extractPathVariables(rSpec.routeSpecification(), request.requestUri.getPath());
		
		// --- Run handler --
		runHandler(request, response, exchange, routeHandler);
	}
	
	/// Runs the given handler, and writes to the response.
	///
	/// @param request Request object
	/// @param exchange Http exchange object in which the response will be written into.
	/// @param routeHandler Route handler to run.
	public void runHandler(HttpRequest request, HttpResponse response, HttpExchange exchange, RouteHandler routeHandler) {
		// Run handler, and if it fails, run the failsafe.
		try {
			response = routeHandler.runHandler(request, response);
		} catch (Exception e) {
			log.error("Handler '{}' failed.", routeHandler, e);
			if (this.internalServerErrorHandler != null) {
				try {
					response = this.internalServerErrorHandler.runHandler(request, new HttpResponse());
				} catch (Exception ie) {
					log.error("The matched request handler has failed, and when executing the internal server error handler, this one failed too. {}", ie.getMessage(), ie);
				}
			}
		}

		// Write response into the exchange object
		try (exchange) {
			this.writeResponse(response, exchange);
		} catch (Exception e) {
			log.error("Couldn't write the response into the exchange object. {}", e.getMessage(), e);
		}
	}

	/// Processes the given filter, and in the case that the filter forwards
	/// into a route handler, runs the handler.
	///
	/// @param request Request object
	/// @param exchange [HttpExchange] object used to write the response into.
	/// @param filter The filter to run.
	/// @return A *continue* flag, meaning, `false` if the request has been forwarded to a new handler *(
	/// meaning that the filter hasn't passed on the request to the next element 
	/// in the chain)*, `true` otherwise *(meaning that the request has been allowed
	/// continue to be passed to the next element in the chain)*. 
	public boolean runFilter(HttpRequest request, HttpResponse response, HttpExchange exchange, RouteFilter filter) {
		// TODO: Rethrow exceptions instead of just returning false?
		Optional<RouteHandler> forcedHandler;
		try {
			forcedHandler = filter.filter(request, response);
		} catch (Exception e) {
			log.error("An error ocurred while running the given filter({}). ", filter, e);
			return false;
		}
		
		if (forcedHandler.isPresent()) {
			try {
				response = forcedHandler.get().runHandler(request, response);
				writeResponse(response, exchange);
			} catch (Exception e) {
				log.error("An error has ocurred while executing the handler ({}) given by the filter.", forcedHandler.get(), e);
			}
			return false;
		}

		return true;
	}

	/// Writes the given response object to the client.
	/// 
	/// @param response Response object containing the data to write.
	/// @param exchange [HttpExchange] object used to write data to the client.
	/// @throws ResponseWriteException If an IO error while writing to the client occurs.
	/// @throws SapphireException If an unhandled exception occurs while writing to the client.
	private void writeResponse(HttpResponse response, HttpExchange exchange) throws SapphireException {
		try {
			HttpUtils.writeResponseToExchange(exchange, response);
		} catch (IOException e) {
			throw new ResponseWriteException("Couldn't write the response object into the HTTP exchange. " + e.getMessage(), e);
		} catch (Exception e) {
			throw new SapphireException("An error has occurred while writing the response into the HTTP exchange. " + e.getMessage(), e);
		}
	}
}
