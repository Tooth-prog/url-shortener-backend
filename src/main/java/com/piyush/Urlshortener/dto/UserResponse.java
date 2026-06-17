package com.piyush.Urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class UserResponse {

    private Integer id;

    private String username;

    private String email;

    private String role;
}
