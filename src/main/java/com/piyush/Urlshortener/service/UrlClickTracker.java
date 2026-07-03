package com.piyush.Urlshortener.service;

import com.piyush.Urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlClickTracker {

    private final UrlRepository urlRepository;

    @Async
    public void incrementClickCountAsync(String shortCode) {
        urlRepository.incrementClickCountByShortCode(shortCode);
    }
}
