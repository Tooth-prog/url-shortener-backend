package com.piyush.Urlshortener.controller;

import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers() {

        return userRepository.findAll();
    }
}
