package com.piyush.Urlshortener.exception;

public class ShortCodeAlreadyExistsException
        extends RuntimeException {


    public ShortCodeAlreadyExistsException(String message) {

        super(message);

    }
}