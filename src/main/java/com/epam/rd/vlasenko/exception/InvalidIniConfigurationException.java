package com.epam.rd.vlasenko.exception;

public class InvalidIniConfigurationException extends RuntimeException {
    public InvalidIniConfigurationException() {
    }

    public InvalidIniConfigurationException(String message) {
        super(message);
    }

    public InvalidIniConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
