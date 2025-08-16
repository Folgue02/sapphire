package io.github.folgue02.sapphire.server.exception;

import io.github.folgue02.sapphire.exception.SapphireException;
import io.github.folgue02.sapphire.router.handler.BaseRouteHandler;

public class HandlerExecutionException extends SapphireException {
    public HandlerExecutionException(Class<? extends BaseRouteHandler> handlerType, String message, Exception cause) {
        super("Execution of handler '" + handlerType + "' has failed. " + message, cause);
    }
}
