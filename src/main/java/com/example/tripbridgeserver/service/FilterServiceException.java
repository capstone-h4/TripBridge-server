package com.example.tripbridgeserver.service;

public class FilterServiceException extends Exception{
    public FilterServiceException(String message) {
        super(message);
    }

    public FilterServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
