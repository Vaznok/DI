package com.epam.rd.vlasenko.exception;

public class InconvertibleTypeException extends DiContainerUncheckedException {
    public InconvertibleTypeException() {
    }

    public InconvertibleTypeException(String message) {
        super(message);
    }

    public InconvertibleTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
