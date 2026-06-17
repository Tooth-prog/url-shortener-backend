package com.piyush.Urlshortener.repository;

import com.piyush.Urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    Optional<Url> findByOriginalUrl(String originalUrl);

    void deleteByExpiryDateBefore(LocalDateTime now);
}