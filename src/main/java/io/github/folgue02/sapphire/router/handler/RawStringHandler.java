package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;

public abstract class RawStringHandler implements RouteHandler<String> {
	@Override
	public HttpResponse processResult(HttpResponse response, String result) {
		response.setBody(result);
		response.status = StatusCode.OK;

		return response;
	}

	@Override
	public String getHandlerDescription() {
		return "A handler which simply returns the string provided by the handle().";
	}
}
