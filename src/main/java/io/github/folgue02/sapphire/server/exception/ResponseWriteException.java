package io.github.folgue02.sapphire.server.exception;

import io.github.folgue02.sapphire.exception.SapphireException;

import java.io.IOException;

public class ResponseWriteException extends SapphireException {
    public ResponseWriteException(String message, IOException ie) {
        super("Couldn't write to the response object. " + message, ie);
    }
}
