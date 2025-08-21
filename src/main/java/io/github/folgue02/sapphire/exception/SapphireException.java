package io.github.folgue02.sapphire.exception;

/// An exception related to the Sapphire web framework.
public class SapphireException extends Exception {
	public SapphireException(String message) {
		super(message);
	}
	public SapphireException(String message, Exception cause) {
		super(message, cause);
	}
}
