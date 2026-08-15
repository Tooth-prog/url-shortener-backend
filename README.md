# URL Shortener 🚀

A production-style URL shortening service built using Spring Boot, PostgreSQL, Redis, JWT Authentication and Docker.

This application allows users to create short URLs, redirect users using short codes, track clicks and manage URL expiration.

---

# Features

✅ Create short URLs  
✅ Generate unique short codes  
✅ Redirect using short codes  
✅ Click tracking  
✅ URL expiry management  
✅ JWT Authentication  
✅ Refresh Token Support  
✅ Role Based Authorization  
✅ Redis Caching  
✅ Scheduled URL Cleanup  
✅ Dockerized Deployment

---

# Tech Stack

## Backend
- Java
- Spring Boot
- Spring Security
- Hibernate / JPA

## Database
- PostgreSQL

## Cache
- Redis

## DevOps
- Docker
- Docker Compose

## API Documentation
- Swagger OpenAPI

---

# System Architecture
            Client
               |
               |
         Spring Boot API
               |
    -----------------------
    |                     |
PostgreSQL Database      Redis Cache
    |                     |
    -----------------------
               |
          URL Shortener Service


# Project Structure
src/main/java/com/piyush/Urlshortener

├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── exception
├── scheduler
└── config


---

# API Endpoints
### Register User
POST
/api/auth/register


### Login User
POST
/api/auth/login

# URL APIs
## Create Short URL
POST
/api/url/shorten
