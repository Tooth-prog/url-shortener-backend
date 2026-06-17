package com.piyush.Urlshortener.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){

        super(message);
    }
}
