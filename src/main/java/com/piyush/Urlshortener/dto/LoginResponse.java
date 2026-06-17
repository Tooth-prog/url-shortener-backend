package com.piyush.Urlshortener.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class LoginResponse {


    private String accessToken;


    private String refreshToken;


}