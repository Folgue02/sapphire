package io.github.folgue02.sapphire.router.handler.impl;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.exchange.StatusCode;
import io.github.folgue02.sapphire.router.handler.RawStringHandler;

public class InlineStringHandler extends RawStringHandler {
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
    public String processInput(HttpRequest request) {
        return this.message;
    }

    @Override
    public String getHandlerDescription() {
        return "Displays a provided text for the client.";
    }
}
