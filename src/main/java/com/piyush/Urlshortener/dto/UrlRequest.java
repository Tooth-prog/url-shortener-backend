package com.piyush.Urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;


import io.swagger.v3.oas.annotations.media.Schema;

@Data

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequest {

    @NotBlank(message = "URL cannot be empty")
    @Schema(description = "The original URL to be shortened")

    @URL(message = "Please provide a valid URL")
    @Pattern(regexp = "^(http|https)://.*$", message = "URL must start with http:// or https://")
    private String url;

    private String customCode;

    private Integer expiryDays;
}
