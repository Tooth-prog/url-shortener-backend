package com.piyush.Urlshortener.controller;

import com.piyush.Urlshortener.dto.UrlRequest;
import com.piyush.Urlshortener.dto.UrlResponse;
import com.piyush.Urlshortener.dto.UrlStatsResponse;
import com.piyush.Urlshortener.service.UrlService;
import com.piyush.Urlshortener.entity.Url;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class UrlController {
    private final UrlService urlService;

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
                "http://localhost:8080/api/" + url.getShortCode(),
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
    public Url analytics(
            @PathVariable String shortCode) {

        return urlService.getAnalytics(shortCode);
    }

    @GetMapping("/stats/{code}")
    public UrlStatsResponse stats(@PathVariable String code) {

        Url url = urlService.getOriginalUrl(code);

        return new UrlStatsResponse(
                url.getOriginalUrl(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiryDate()
        );
    }
}
