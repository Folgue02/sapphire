package io.github.folgue02.sapphire.router.handler.impl;

import io.github.folgue02.sapphire.consts.HeaderConsts;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;
import io.github.folgue02.sapphire.router.handler.RouteHandler;

public final class RedirectHandler extends RouteHandler {
	private final String targetRoute;

	public RedirectHandler(String targetRoute) {
		this.targetRoute = targetRoute;
	}

	@Override
	public HttpResponse runHandler(HttpRequest _request, HttpResponse response) throws Exception {
		response.status = StatusCode.PERMANENT_REDIRECT;
		response.putHeader(HeaderConsts.LOCATION, this.targetRoute);

		return response;
	}

	@Override
	public String getHandlerDescription() {
		return "Redirects the user to a given routeSpecification.";
	}
}
