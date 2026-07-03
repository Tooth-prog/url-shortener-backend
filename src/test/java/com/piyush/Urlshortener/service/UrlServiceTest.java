package com.piyush.Urlshortener.service;


import com.piyush.Urlshortener.entity.Url;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.exception.ShortCodeAlreadyExistsException;
import com.piyush.Urlshortener.exception.UrlExpiredException;
import com.piyush.Urlshortener.exception.UrlNotFoundException;
import com.piyush.Urlshortener.repository.UrlRepository;
import com.piyush.Urlshortener.repository.UserRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {



    @Mock
    private UrlRepository urlRepository;


    @Mock
    private UserRepository userRepository;


    @Mock
    private RedisTemplate<String,String> redisTemplate;


    @Mock
    private ValueOperations<String,String> valueOperations;


    @Mock
    private UrlClickTracker urlClickTracker;



    @InjectMocks
    private UrlService urlService;






    @BeforeEach
    void setup(){


        SecurityContextHolder
                .getContext()
                .setAuthentication(

                        new UsernamePasswordAuthenticationToken(
                                "test@gmail.com",
                                null
                        )

                );

    }









    @Test
    void shouldReturnOriginalUrl(){



        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);



        when(valueOperations.get("abc123"))
                .thenReturn(null);





        Url url = Url.builder()

                .shortCode("abc123")

                .originalUrl("https://google.com")

                .clickCount(0L)

                .expiryDate(
                        LocalDateTime.now()
                                .plusDays(5)
                )

                .build();




        when(urlRepository.findByShortCode("abc123"))

                .thenReturn(Optional.of(url));





        Url result =
                urlService.getOriginalUrl("abc123");




        assertEquals(

                "https://google.com",

                result.getOriginalUrl()

        );

    }









    @Test
    void shouldThrowExceptionWhenUrlNotFound(){



        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);



        when(valueOperations.get("xyz123"))
                .thenReturn(null);




        when(urlRepository.findByShortCode("xyz123"))

                .thenReturn(Optional.empty());





        assertThrows(

                UrlNotFoundException.class,

                () ->
                        urlService.getOriginalUrl("xyz123")

        );

    }









    @Test
    void shouldThrowExceptionWhenUrlExpired(){



        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);



        when(valueOperations.get("old123"))
                .thenReturn(null);





        Url expiredUrl = Url.builder()

                .shortCode("old123")

                .originalUrl("https://expired.com")

                .expiryDate(
                        LocalDateTime.now()
                                .minusDays(1)
                )

                .build();





        when(urlRepository.findByShortCode("old123"))

                .thenReturn(Optional.of(expiredUrl));





        assertThrows(

                UrlExpiredException.class,

                () ->
                        urlService.getOriginalUrl("old123")

        );


    }









    @Test
    void shouldRejectDuplicateShortCode(){





        Url existing = Url.builder()

                .shortCode("abc123")

                .build();





        when(urlRepository.findByShortCode("abc123"))

                .thenReturn(Optional.of(existing));





        assertThrows(

                ShortCodeAlreadyExistsException.class,

                () ->
                        urlService.shortenUrl(

                                "https://facebook.com",

                                "abc123",

                                7

                        )

        );


    }









    @Test
    void shouldCreateNewShortUrl(){



        when(urlRepository.findByOriginalUrl(
                "https://github.com"
        ))
                .thenReturn(Optional.empty());





        when(urlRepository.findByShortCode(anyString()))

                .thenReturn(Optional.empty());








        Url saved = Url.builder()

                .id(1L)

                .shortCode("abc789")

                .originalUrl("https://github.com")

                .build();





        when(urlRepository.save(any(Url.class)))

                .thenReturn(saved);





        when(redisTemplate.opsForValue())

                .thenReturn(valueOperations);





        Url result =

                urlService.shortenUrl(

                        "https://github.com",

                        null,

                        7

                );





        assertNotNull(result);



        assertEquals(

                "https://github.com",

                result.getOriginalUrl()

        );


    }


}