package com.piyush.Urlshortener.service;


import com.piyush.Urlshortener.entity.Url;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.exception.ShortCodeAlreadyExistsException;
import com.piyush.Urlshortener.exception.UrlExpiredException;
import com.piyush.Urlshortener.exception.UrlNotFoundException;
import com.piyush.Urlshortener.repository.UrlRepository;

import com.piyush.Urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
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



    public Url shortenUrl(
            String originalUrl,
            String customCode,
            Integer expiryDays) {



        String shortCode;



        // 1. Custom code provided
        if(customCode != null && !customCode.isBlank()) {


            shortCode = customCode;


        } else {


            // 2. Check existing URL only when no custom code

            Optional<Url> existingUrl =
                    urlRepository.findByOriginalUrl(originalUrl);


            if(existingUrl.isPresent()) {

                return existingUrl.get();

            }


            shortCode = generateShortCode();

        }




        // 3. Check short code availability

        if(urlRepository.findByShortCode(shortCode).isPresent()) {


            throw new ShortCodeAlreadyExistsException(
                    "Short code already exists"
            );

        }




        // 4. Default expiry

        if(expiryDays == null) {

            expiryDays = 7;

        }



        LocalDateTime expiryDate =
                LocalDateTime.now()
                        .plusDays(expiryDays);




        // 5. Create URL object
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Url url = Url.builder()

                .originalUrl(originalUrl)

                .shortCode(shortCode)

                .createdAt(LocalDateTime.now())

                .clickCount(0L)

                .expiryDate(expiryDate)

                .build();




        // 6. Save database

        Url savedUrl =
                urlRepository.save(url);




        // 7. Save Redis cache

        redisTemplate.opsForValue().set(

                savedUrl.getShortCode(),

                savedUrl.getOriginalUrl(),

                Duration.ofHours(24)

        );



        return savedUrl;

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







    public Url getOriginalUrl(String shortCode) {



        Url url =
                urlRepository.findByShortCode(shortCode)

                        .orElseThrow(() ->
                                new UrlNotFoundException(
                                        "Short URL not found"
                                )
                        );




        // expiry check

        if(url.getExpiryDate() != null &&

                url.getExpiryDate()
                        .isBefore(LocalDateTime.now())) {



            throw new UrlExpiredException(
                    "Short URL expired"
            );

        }





        // click tracking

        url.setClickCount(

                Optional.ofNullable(url.getClickCount())
                        .orElse(0L)
                        + 1

        );



        urlRepository.save(url);





        // refresh redis

        redisTemplate.opsForValue().set(

                shortCode,

                url.getOriginalUrl(),

                Duration.ofHours(24)

        );



        return url;

    }







    public Url getAnalytics(String shortCode) {


        return urlRepository.findByShortCode(shortCode)

                .orElseThrow(() ->
                        new UrlNotFoundException(
                                "Short URL not found"
                        )
                );

    }

}