# URL Shortener Backend

A production-style URL shortening service built using Spring Boot, PostgreSQL, Redis and Docker.

## Features

- Create short URLs
- Redirect using short code
- Click tracking
- Expiry support
- JWT Authentication
- PostgreSQL persistence
- Redis caching
- Dockerized deployment


## Tech Stack

Backend:
- Java
- Spring Boot
- Spring Security
- Hibernate/JPA


Database:
- PostgreSQL


Cache:
- Redis


DevOps:
- Docker
- Docker Compose


## Architecture

Client
|
Spring Boot API
|
----------------
|              |
PostgreSQL    Redis


## API Endpoints

### Create Short URL

POST

/api/shorten


Request:

```json
{
"url":"https://google.com",
"expiryDays":7
}