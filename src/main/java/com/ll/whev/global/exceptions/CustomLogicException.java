package com.ll.whev.global.exceptions;

public class CustomLogicException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public CustomLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}