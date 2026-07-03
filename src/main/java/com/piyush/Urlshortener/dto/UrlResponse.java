package com.piyush.Urlshortener.dto;


import lombok.*;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
public class UrlResponse {

    @Schema(description = "The original URL that was shortened", example = "https://www.example.com/some/long/url")
    private String shortCode;

    @Schema(description = "The shortened URL that can be used to access the original URL", example = "http://short.ly/abc123")
    private String shortUrl;

    private LocalDateTime expiryDate;

}