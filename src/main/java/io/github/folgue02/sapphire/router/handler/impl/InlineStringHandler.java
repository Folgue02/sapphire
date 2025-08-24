package io.github.folgue02.sapphire.router.handler.impl;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;
import io.github.folgue02.sapphire.router.handler.RawStringHandler;

public class InlineStringHandler extends RawStringHandler<Boolean> {
    private final String message;
    private final StatusCode returnStatusCode;

    public InlineStringHandler(String message) {
        this.message = message;
        this.returnStatusCode = StatusCode.OK;
    }

    public InlineStringHandler(String message, StatusCode returnStatusCode) {
        this.message = message;
        this.returnStatusCode = returnStatusCode;
    }

	@Override
	public Boolean handleInput(HttpRequest request) throws Exception {
		return false;
	}

	@Override
	public String run(HttpRequest _request, HttpResponse _response, Boolean _processedInput) throws Exception {
		return message;
	}

	@Override
    public String getHandlerDescription() {
        return "Displays a provided text for the client.";
    }
}
