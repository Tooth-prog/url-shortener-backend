package com.piyush.Urlshortener.service;

import com.piyush.Urlshortener.entity.Url;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<Url> getUserUrls() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUrls();
    }
}
