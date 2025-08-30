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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SunSapphireServer extends SapphireServer implements HttpHandler {

	public SunSapphireServer(Router router, InetSocketAddress address) {
		super(router, address);
	}

	@Override
	public void run() throws Exception {
		var server = HttpServer.create(this.address, 0);

		server.createContext("/", this);

		server.start();
	}

	@Override
	public void handle(HttpExchange exchange) {
		final String path = exchange.getHttpContext().getPath();
		final RouteSpecification rSpec;
		final RouteHandler routeHandler;

		final HttpRequest request;
		final HttpResponse response = new HttpResponse();
		try {
			request = HttpUtils.requestFromExchange(exchange);
		} catch (Exception e) {
			System.out.println("Couldn't read from request.");
			e.printStackTrace();
			return;
		}

		List<RouteFilter> matchedRouteFilters = this.router.findMatchedFilters(request);

		// If a filter corresponds to the giving request, it is executed.
		// If the filter decides to forward the request to a different handler, this one is 
		// executed, and whatever other handler that would have been chosen for the request is ignored.
		for (var filter : matchedRouteFilters) {
			boolean continueChain = runFilter(request, response, exchange, filter);
			if (!continueChain)
				return;
		}

		// Run handler
		Optional<Map.Entry<RouteSpecification, RouteHandler>> handlerOpt = this.router.findMatchedRoute(request);
		if (handlerOpt.isEmpty()) {
			routeHandler = this.noMappingFoundHandler;
			rSpec = new RouteSpecification(request.method, request.requestUri.getPath());
		} else {
			rSpec = handlerOpt.get().getKey();
			routeHandler = handlerOpt.get().getValue();
		}

		if (!runHandler(request, response, exchange, routeHandler)) {
			System.err.printf("The route handler (%s) has failed.\n", routeHandler);
		}
	}

	/// Runs the given handler, and writes to the response.
	///
	/// @param request Request object
	/// @param exchange Http exchange object in which the response will be written into.
	/// @param routeHandler Route handler to run.
	/// @return `true` if the handler has been executed without errors, `false` otherwise.
	public boolean runHandler(HttpRequest request, HttpResponse response, HttpExchange exchange, RouteHandler routeHandler) {
		try (exchange) {
			response = routeHandler.runHandler(request, response);
			this.writeResponse(response, exchange);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't handle request and write to response.");
			return false;
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
			System.err.printf("An error ocurred while running the given filter(%s). \n", filter);
			e.printStackTrace();
			return false;
		}
		
		if (forcedHandler.isPresent()) {
			try {
				response = forcedHandler.get().runHandler(request, response);
				writeResponse(response, exchange);
			} catch (Exception e) {
				System.err.printf("An error has ocurred while executing the handler (%s) given by the filter.", forcedHandler.get());
				e.printStackTrace();
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
