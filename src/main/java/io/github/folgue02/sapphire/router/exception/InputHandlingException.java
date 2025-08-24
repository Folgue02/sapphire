package io.github.folgue02.sapphire.router.exception;

import io.github.folgue02.sapphire.exception.SapphireException;

/// Thrown when an error occurs while handling the input of the request.
public class InputHandlingException extends SapphireException {
	public InputHandlingException(String message) {
		super("Error while handling the input of the request: '%s'".formatted(message));
	}

	public InputHandlingException(Exception cause) {
		super("Error while handling the input of the request: '%s'".formatted(cause.getMessage()), cause);
	}

	public InputHandlingException(String message, Exception cause) {
		super("Error while handling the input of the request: '%s'".formatted(message), cause);
	}
}
