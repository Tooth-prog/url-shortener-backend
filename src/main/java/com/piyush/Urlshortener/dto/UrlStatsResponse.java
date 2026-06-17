package com.piyush.Urlshortener.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class UrlStatsResponse {

    private String originalUrl;

    private Long clicks;

    private LocalDateTime createdAt;

    private LocalDateTime expiryDate;
}
