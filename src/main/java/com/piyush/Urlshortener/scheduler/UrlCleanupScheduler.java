package com.piyush.Urlshortener.scheduler;

import com.piyush.Urlshortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UrlCleanupScheduler {

    private final UrlRepository urlRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteExpiredUrls() {
        urlRepository
                .deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
