package com.piyush.Urlshortener.dto;


import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class UrlResponse {


    private String shortCode;

    private String shortUrl;

    private LocalDateTime expiryDate;

}