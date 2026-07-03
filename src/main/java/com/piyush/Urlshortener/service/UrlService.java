package com.piyush.Urlshortener.service;


import com.piyush.Urlshortener.entity.Url;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.exception.ShortCodeAlreadyExistsException;
import com.piyush.Urlshortener.exception.UrlExpiredException;
import com.piyush.Urlshortener.exception.UrlNotFoundException;
import com.piyush.Urlshortener.repository.UrlRepository;
import com.piyush.Urlshortener.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class UrlService {


    private final UrlRepository urlRepository;

    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final UrlClickTracker urlClickTracker;


    private static final Logger log =
            LoggerFactory.getLogger(UrlService.class);



    public Url shortenUrl(
            String originalUrl,
            String customCode,
            Integer expiryDays) {


        log.info("Creating short URL for: {}", originalUrl);



        String shortCode;



        // Custom code provided

        if(customCode != null && !customCode.isBlank()) {


            shortCode = customCode;


            log.info("Using custom short code: {}", shortCode);



        } else {



            Optional<Url> existingUrl =
                    urlRepository.findByOriginalUrl(originalUrl);



            if(existingUrl.isPresent()) {


                log.info(
                        "Existing short URL returned for {}",
                        originalUrl
                );


                return existingUrl.get();

            }



            shortCode = generateShortCode();


            log.info(
                    "Generated short code: {}",
                    shortCode
            );

        }





        // Check duplicate short code


        if(urlRepository.findByShortCode(shortCode).isPresent()) {


            log.error(
                    "Short code already exists: {}",
                    shortCode
            );


            throw new ShortCodeAlreadyExistsException(
                    "Short code already exists"
                );

        }







        // Default expiry


        if(expiryDays == null) {


            expiryDays = 7;


            log.info(
                    "Expiry not provided. Default expiry set to 7 days"
            );

        }




        LocalDateTime expiryDate =
                LocalDateTime.now()
                        .plusDays(expiryDays);






        // Get logged in user (support anonymous shortening)
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String email = authentication.getName();
            user = userRepository.findByEmail(email).orElse(null);
        }



        Url url = Url.builder()


                .originalUrl(originalUrl)

                .shortCode(shortCode)

                .createdAt(LocalDateTime.now())

                .clickCount(0L)

                .expiryDate(expiryDate)

                .user(user)

                .build();







        // Save database


        Url savedUrl =
                urlRepository.save(url);



        log.info(
                "URL saved successfully with id: {}",
                savedUrl.getId()
        );







        // Save Redis with dynamic TTL
        long ttlSeconds = Duration.between(LocalDateTime.now(), expiryDate).getSeconds();
        if (ttlSeconds > 0) {
            long cacheSeconds = Math.min(ttlSeconds, 24 * 3600);
            redisTemplate.opsForValue().set(
                    savedUrl.getShortCode(),
                    savedUrl.getOriginalUrl(),
                    Duration.ofSeconds(cacheSeconds)
            );
            log.info("URL cached in Redis with TTL {}s", cacheSeconds);
        }


        return savedUrl;

    }








    public Url getOriginalUrl(String shortCode) {

        log.info(
                "Fetching original URL for short code: {}",
                shortCode
        );

        //1.Check Redis First
        String cachedOriginalUrl = redisTemplate.opsForValue().get(shortCode);
        if(cachedOriginalUrl != null) {
            log.info(
                    "Cache hit for short code: {}",
                    shortCode
            );

            // Async click tracking
            urlClickTracker.incrementClickCountAsync(shortCode);

            return Url.builder()
                            .shortCode(shortCode)
                            .originalUrl(cachedOriginalUrl)
                            .build();
        }

        //2. Redis miss -> Database
        log.info(
                "Cache miss for short code: {}. Fetching from database.",
                shortCode
        );



        Url url =
                urlRepository.findByShortCode(shortCode)

                        .orElseThrow(() -> {

                            log.error(
                                    "Short URL not found: {}",
                                    shortCode
                            );


                            return new UrlNotFoundException(
                                    "Short URL not found"
                            );

                        });







        // expiry check


        if(url.getExpiryDate() != null &&

                url.getExpiryDate()
                        .isBefore(LocalDateTime.now())) {



            log.error(
                    "URL expired: {}",
                    shortCode
            );



            throw new UrlExpiredException(
                    "Short URL expired"
            );

        }

        // Async click tracking
        urlClickTracker.incrementClickCountAsync(shortCode);
        url.setClickCount(Optional.ofNullable(url.getClickCount()).orElse(0L) + 1);

        // Save in Redis with remaining TTL
        long ttlSeconds = Duration.between(LocalDateTime.now(), url.getExpiryDate()).getSeconds();
        if (ttlSeconds > 0) {
            long cacheSeconds = Math.min(ttlSeconds, 24 * 3600);
            redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), Duration.ofSeconds(cacheSeconds));
            log.info(
                    "URL cached in Redis after database fetch for short code: {} with TTL {}s",
                    shortCode,
                    cacheSeconds
            );
        }

        return url;

    }









    public Url getAnalytics(String shortCode) {



        log.info(
                "Fetching analytics for {}",
                shortCode
        );



        return urlRepository.findByShortCode(shortCode)

                .orElseThrow(() -> {


                    log.error(
                            "Analytics not found for {}",
                            shortCode
                    );


                    return new UrlNotFoundException(
                            "Short URL not found"
                    );

                });

    }









    private String generateShortCode() {



        String chars =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";



        Random random = new Random();



        StringBuilder code =
                new StringBuilder();




        for(int i = 0; i < 6; i++) {


            code.append(
                    chars.charAt(
                            random.nextInt(chars.length())
                    )
            );

        }



        return code.toString();

    }

}