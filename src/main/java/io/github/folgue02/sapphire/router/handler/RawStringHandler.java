package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;

public abstract class RawStringHandler<IN> extends IORouteHandler<IN, String>  {
	@Override
	public final HttpResponse handleOutput(HttpResponse response, String output) throws Exception {
		response.setBody(output);
		return response;
	}

	@Override
	public String getHandlerDescription() {
		return "A handler which simply returns the string provided by the handle().";
	}
}
