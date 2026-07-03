package com.piyush.Urlshortener.service;

import com.piyush.Urlshortener.dto.LoginResponse;
import com.piyush.Urlshortener.dto.RegisterRequest;
import com.piyush.Urlshortener.dto.UserResponse;
import com.piyush.Urlshortener.entity.RefreshToken;
import com.piyush.Urlshortener.entity.Role;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.exception.InvalidCredentialsException;
import com.piyush.Urlshortener.exception.UserNotFoundException;
import com.piyush.Urlshortener.exception.UserAlreadyExistsException;
import com.piyush.Urlshortener.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is already registered");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    public LoginResponse login(String email, String password) {


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));



        if (!passwordEncoder.matches(password, user.getPassword())) {

            throw new InvalidCredentialsException("Invalid password");

        }



        String accessToken =
                jwtService.generateToken(user.getEmail(), user.getRole().name());



        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);



        return new LoginResponse(

                accessToken,

                refreshToken.getToken()

        );

    }

}
