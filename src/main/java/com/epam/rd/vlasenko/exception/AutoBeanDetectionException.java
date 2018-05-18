package com.epam.rd.vlasenko.exception;

public class AutoBeanDetectionException extends DiContainerUncheckedException {
    public AutoBeanDetectionException() {
    }

    public AutoBeanDetectionException(String message) {
        super(message);
    }

    public AutoBeanDetectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
