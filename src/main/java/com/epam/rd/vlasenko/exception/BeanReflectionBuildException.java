package com.epam.rd.vlasenko.exception;

public class BeanReflectionBuildException extends DiContainerUncheckedException {
    public BeanReflectionBuildException() {
    }

    public BeanReflectionBuildException(String message) {
        super(message);
    }

    public BeanReflectionBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
