package com.piyush.Urlshortener.exception;


public class UrlExpiredException
        extends RuntimeException {


    public UrlExpiredException(String message){

        super(message);

    }

}