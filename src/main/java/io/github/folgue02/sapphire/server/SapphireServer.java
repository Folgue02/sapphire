package io.github.folgue02.sapphire.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.github.folgue02.sapphire.SapphireConsts;
import io.github.folgue02.sapphire.exception.SapphireException;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.router.RouteSpecification;
import io.github.folgue02.sapphire.router.Router;
import io.github.folgue02.sapphire.router.handler.BaseRouteHandler;
import io.github.folgue02.sapphire.server.exception.ResponseWriteException;
import io.github.folgue02.sapphire.utils.HttpUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class SapphireServer implements HttpHandler {
    private final Router router;
    private final InetSocketAddress address;
    private BaseRouteHandler<?> notFoundHandler;
    private BaseRouteHandler<?> internalServerError;

    public SapphireServer(Router router, InetSocketAddress address) {
        this.address = address;
        this.router = router;
        this.notFoundHandler = SapphireConsts.DEFAULT_NOT_FOUND_HANDLER;
        this.internalServerError = SapphireConsts.DEFAULT_INTERNAL_ERROR_HANDLER;
    }

    public void run() throws Exception {
        var server = HttpServer.create(this.address, 0);

        server.createContext("/", this);

        server.start();
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("TEST");
        final String path = exchange.getHttpContext().getPath();
        final RouteSpecification rSpec;
        final BaseRouteHandler routeHandler;

        HttpRequest request;
        try {
            request = HttpUtils.requestFromExchange(exchange);
        } catch (Exception e) {
            System.out.println("Couldn't read from request.");
            return;
        }

        Optional<Map.Entry<RouteSpecification, BaseRouteHandler>> handlerOpt = this.router.findMatchedRoute(request);

        if (handlerOpt.isEmpty()) {
            routeHandler = this.notFoundHandler;
            rSpec = new RouteSpecification(request.getMethod(), request.getRequestUri().getPath());
        } else {
            rSpec = handlerOpt.get().getKey();
            routeHandler = handlerOpt.get().getValue();
        }

        try (exchange) {
            var response = routeHandler.runHandler(request);
            this.writeResponse(response, exchange);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't handle request and write to response.");
        }
    }

    private void writeResponse(HttpResponse response, HttpExchange exchange) throws SapphireException {
        try (OutputStream os = exchange.getResponseBody()) {
            HttpUtils.writeResponseToExchange(exchange, response);
        } catch (IOException e) {
            throw new ResponseWriteException("Couldn't write the response object into the HTTP exchange. " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SapphireException("An error has occurred while writing the response into the HTTP exchange. " + e.getMessage(), e);
        }
    }
}
