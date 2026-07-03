package com.piyush.Urlshortener.exception;


import com.piyush.Urlshortener.dto.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =  LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleUserNotFound(
            UserNotFoundException ex
    ){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );

    }



    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleInvalidCredentials(
            InvalidCredentialsException ex
    ){

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );

    }




    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleUrlNotFound(
            UrlNotFoundException ex
    ){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );

    }




    @ExceptionHandler(ShortCodeAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleShortCodeAlreadyExists(
            ShortCodeAlreadyExistsException ex
    ){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );

    }





    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleUrlExpired(
            UrlExpiredException ex
    ){

        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(
                        new ApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );

    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleValidationErrors(
            MethodArgumentNotValidException ex
    ){


        Map<String,String> errors = new HashMap<>();


        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->

                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )

                );



        return ResponseEntity
                .badRequest()
                .body(
                        new ApiResponse<>(
                                false,
                                "Validation failed",
                                errors
                        )
                );

    }





    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        new ApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("Unexpected Exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ApiResponse<>(
                                false,
                                "An unexpected error occurred.",
                                null
                        )
                );
    }
}