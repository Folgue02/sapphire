package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;

public abstract class RawStringHandler extends BaseRouteHandler<String> {
    @Override
    public HttpResponse processResult(HttpResponse response, String result) {
        response.setBody(result);
        response.setStatus(StatusCode.OK);

        return response;
    }

    @Override
    public String getHandlerDescription() {
        return "A handler which simply returns the string provided by the handle().";
    }
}
