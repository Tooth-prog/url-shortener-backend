package com.piyush.Urlshortener.controller;

import com.piyush.Urlshortener.dto.UrlRequest;
import com.piyush.Urlshortener.dto.UrlResponse;
import com.piyush.Urlshortener.dto.UrlStatsResponse;
import com.piyush.Urlshortener.service.UrlService;
import com.piyush.Urlshortener.entity.Url;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import java.net.URI;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "URL Management", description = "Endpoints for managing URLs")

public class UrlController {
    private final UrlService urlService;

    private static final Logger log = LoggerFactory.getLogger(UrlController.class);

    @Operation(summary = "Shorten a URL", description = "Creates a short URL with optional custom code and expiry.")
    @PostMapping("/shorten")
    public UrlResponse shorten(@Valid @RequestBody UrlRequest request) {

        Url url = urlService.shortenUrl(
                request.getUrl(),
                request.getCustomCode(),
                request.getExpiryDays()
        );

        return new UrlResponse(
                url.getShortCode(),
                "http://localhost:8080/api/v1/" + url.getShortCode(),
                url.getExpiryDate()
        );
    }

    @Operation(summary = "Redirect using short URL", description = "Redirects user to original URL ")
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        Url url = urlService.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }

    @GetMapping("/analytics/{shortCode}")
    public UrlStatsResponse analytics(@PathVariable String shortCode) {

        Url url = urlService.getAnalytics(shortCode);
        return new UrlStatsResponse(
                url.getOriginalUrl(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiryDate()
        );
    }

    @GetMapping("/stats/{code}")
    public UrlStatsResponse stats(@PathVariable String code) {

        Url url = urlService.getAnalytics(code);

        return new UrlStatsResponse(
                url.getOriginalUrl(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiryDate()
        );
    }
}
