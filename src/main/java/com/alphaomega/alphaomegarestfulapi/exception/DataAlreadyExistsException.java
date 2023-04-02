package com.alphaomega.alphaomegarestfulapi.exception;

public class DataAlreadyExistsException extends RuntimeException{
    public DataAlreadyExistsException(String message) {
        super(message);
    }
}
