package io.github.folgue02.sapphire.exception;

public class SapphireException extends Exception {
    public SapphireException(String message) {
        super(message);
    }
    public SapphireException(String message, Exception cause) {
        super(message, cause);
    }
}
