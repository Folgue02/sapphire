package io.github.folgue02.sapphire.utils;

import com.sun.net.httpserver.HttpExchange;
import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class HttpUtils {
    private HttpUtils() {}

    public static HttpRequest requestFromExchange(HttpExchange exchange) throws IOException {
        var request = new HttpRequest();

        var uri = exchange.getRequestURI();
        var requestBody = exchange.getRequestBody().readAllBytes();
        var headers = new HashMap<>(exchange.getRequestHeaders());
        var method = HttpMethod.ofString(exchange.getRequestMethod());

        request.setHeaders(headers);
        request.setRequestUri(uri);
        request.setMethod(method);
        request.setBody(requestBody);
        request.setPathQueryAttributes(RouteUtils.parseQueryAttributes(uri.getRawQuery()));

        return request;
    }

    public static void writeResponseToExchange(HttpExchange exchange, HttpResponse response) throws IOException {
        if (response.getHeaders() != null) {
            for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
                exchange.getResponseHeaders().set(entry.getKey(), entry.getValue());
            }
        }

        byte[] body = Objects.requireNonNullElse(response.getBody(), new byte[0]);

        exchange.sendResponseHeaders(response.getStatus().getStatusCode(), body.length);

        try (OutputStream os = exchange.getResponseBody()) {
            if (body.length > 0) {
                os.write(body);
            }
            os.flush();
        }
    }

    public static String decodeHttpComponent(String comp) {
        try {
            return URLDecoder.decode(comp, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return comp;
        }
    }
}
