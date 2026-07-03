package com.piyush.Urlshortener.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls", indexes = {
        @Index(name = "idx_urls_short_code", columnList = "shortCode", unique = true),
        @Index(name = "idx_urls_expiry_date", columnList = "expiryDate"),
        @Index(name = "idx_urls_original_url", columnList = "originalUrl")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Builder.Default
    @Column(nullable = false)
    private Long clickCount = 0L;

    @Column(unique = true, nullable = false)
    private String shortCode;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}