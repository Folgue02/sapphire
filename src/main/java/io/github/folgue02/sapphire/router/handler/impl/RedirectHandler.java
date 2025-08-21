package io.github.folgue02.sapphire.router.handler.impl;

import io.github.folgue02.sapphire.consts.HeaderConsts;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;
import io.github.folgue02.sapphire.router.handler.BaseRouteHandler;

public final class RedirectHandler extends BaseRouteHandler<Boolean> {
    private final String targetRoute;

    public RedirectHandler(String targetRoute) {
        this.targetRoute = targetRoute;
    }

    @Override
    public Boolean handleRequest(HttpRequest request, HttpResponse response) throws Exception {
        return false;
    }

    @Override
    public HttpResponse processResult(HttpResponse response, Boolean result) throws Exception {
        response.status = StatusCode.PERMANENT_REDIRECT;
        response.putHeader(HeaderConsts.LOCATION, this.targetRoute);

        return response;
    }

    @Override
    public String getHandlerDescription() {
        return "Redirects the user to a given routeSpecification.";
    }
}
