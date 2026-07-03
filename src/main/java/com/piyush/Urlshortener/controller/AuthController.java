package com.piyush.Urlshortener.controller;


import com.piyush.Urlshortener.dto.*;
import com.piyush.Urlshortener.entity.RefreshToken;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.service.AuthService;
import com.piyush.Urlshortener.service.JwtService;
import com.piyush.Urlshortener.service.RefreshTokenService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;



    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return authService.register(request);

    }



    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {


        LoginResponse response = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return new ApiResponse<>(
                true,
                "Login successful",
                response
        );

    }




    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(
            @RequestBody RefreshRequest request
    ){


        RefreshToken refreshToken =
                refreshTokenService
                        .findByToken(
                                request.getRefreshToken()
                        );



        refreshTokenService
                .verifyExpiration(refreshToken);


        User user = refreshToken.getUser();
        String newAccessToken =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().name()
                );



        return new ApiResponse<>(
                true,
                "Token refreshed successfully",
                new LoginResponse(
                        newAccessToken,
                        refreshToken.getToken()
                )
        );

    }

}