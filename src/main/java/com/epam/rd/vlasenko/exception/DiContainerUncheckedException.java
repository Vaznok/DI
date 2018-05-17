package com.epam.rd.vlasenko.exception;

public class DiContainerUncheckedException extends RuntimeException {
    public DiContainerUncheckedException() {
    }

    public DiContainerUncheckedException(String message) {
        super(message);
    }

    public DiContainerUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }
}
