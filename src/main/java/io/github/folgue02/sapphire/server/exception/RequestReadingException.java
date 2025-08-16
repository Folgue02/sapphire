package io.github.folgue02.sapphire.server.exception;

import io.github.folgue02.sapphire.exception.SapphireException;

public class RequestReadingException extends SapphireException {
	public RequestReadingException(Exception cause) {
		super("Couldn't read from the request object. " + cause.getMessage(), cause);
	}
}
