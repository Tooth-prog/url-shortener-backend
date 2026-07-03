package com.piyush.Urlshortener.controller;

import com.piyush.Urlshortener.dto.UserUrlResponse;
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
    public List<UserUrlResponse> getUrls() {
        return userService.getUserUrls().stream()
                .map(url -> new UserUrlResponse(
                        url.getId(),
                        url.getOriginalUrl(),
                        url.getShortCode(),
                        "http://localhost:8080/api/v1/" + url.getShortCode(),
                        url.getClickCount(),
                        url.getCreatedAt(),
                        url.getExpiryDate()
                ))
                .toList();
    }
}
