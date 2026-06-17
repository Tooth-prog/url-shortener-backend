package com.piyush.Urlshortener.controller;

import com.piyush.Urlshortener.entity.Url;
import com.piyush.Urlshortener.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/urls")
    public List<Url> getUrls() {

        return userService.getUserUrls();
    }
}
