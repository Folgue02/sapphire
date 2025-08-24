package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;

/// Dumb handler, designed to be left as a **placeholder** in the router.
///
/// Simply returns a dummy text.
public class DumbHandler extends RawStringHandler<Boolean> {
	@Override
	public Boolean handleInput(HttpRequest request) throws Exception {
		return false;
	}

	@Override
	public String run(HttpRequest _request, HttpResponse _response, Boolean _processedInput) throws Exception {
		return "Dumb handler; TODO: Replace.";
	}

	@Override
	public String getHandlerDescription() {
		return "A dumb handler, which does nothing (TODO: Replace).";
	}
}
