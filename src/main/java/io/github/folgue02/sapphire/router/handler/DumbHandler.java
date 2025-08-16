package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;

/// Dumb handler, designed to be left as a **placeholder** in the router.
///
/// Simply returns a dummy text.
public class DumbHandler extends RawStringHandler {
	@Override
	public String handleRequest(HttpRequest request, HttpResponse response) {
		return "<h1>Dumb handler response! To be replaced!</h1>";
	}

	@Override
	public String getHandlerDescription() {
		return "A dumb handler, which does nothing (TODO: Replace).";
	}
}
