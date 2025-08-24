package io.github.folgue02.sapphire.router.exception;

import io.github.folgue02.sapphire.exception.SapphireException;

/// Thrown when an error occurs while handling the output of the request.
public class OutputHandlingException extends SapphireException {
	public OutputHandlingException(String message) {
		super("Error while handling the generated output of the request: '%s'".formatted(message));
	}

	public OutputHandlingException(Exception cause) {
		super("Error while handling the generated output of the request: '%s'".formatted(cause.getMessage()), cause);
	}

	public OutputHandlingException(String message, Exception cause) {
		super("Error while handling the generated output of the request: '%s'".formatted(message), cause);
	}
}
