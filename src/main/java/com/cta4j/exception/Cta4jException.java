package com.cta4j.exception;

public class Cta4jException extends RuntimeException {
    public Cta4jException(String message) {
        super(message);
    }

    public Cta4jException(String message, Throwable cause) {
        super(message, cause);
    }
}
