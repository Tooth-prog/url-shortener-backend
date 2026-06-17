package com.piyush.Urlshortener.service;


import com.piyush.Urlshortener.entity.RefreshToken;
import com.piyush.Urlshortener.entity.User;
import com.piyush.Urlshortener.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;



    public RefreshToken createRefreshToken(User user){


        RefreshToken refreshToken =
                RefreshToken.builder()

                        .token(UUID.randomUUID().toString())

                        .expiresAt(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )

                        .user(user)

                        .build();


        return refreshTokenRepository.save(refreshToken);

    }



    public RefreshToken findByToken(String token){


        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Refresh token not found"
                        )
                );

    }




    public RefreshToken verifyExpiration(
            RefreshToken refreshToken
    ){


        if(refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())){


            refreshTokenRepository.delete(refreshToken);


            throw new RuntimeException(
                    "Refresh token expired"
            );

        }


        return refreshToken;

    }

}