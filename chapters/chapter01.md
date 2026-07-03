# Chapter 1: Project Overview

## Introduction to URL Shortening
URL Shorteners are utilities that translate long, complex URLs into compact, readable aliases. A typical URL might look like:
`https://www.google.com/search?q=spring+boot+mvc+data+jpa+docker+redis+jwt+security+architectures`
Which a URL Shortener can convert to:
`http://localhost:8080/api/v1/abc123`

### What is this Project?
This project is an enterprise-grade URL Shortener web service built using Spring Boot. It provides a RESTful API to shorten URLs, redirect requests using short codes, maintain analytics on clicks, and secure endpoints using JWT authentication. It leverages PostgreSQL for persistent storage and Redis for caching to enable high-throughput redirections.

### Why URL Shorteners Exist
1. **Aesthetics & Simplicity**: Long URLs are visually unappealing, occupy too much space, and look suspicious.
2. **Character Limits**: Modern messaging apps and social platforms (like Twitter/X, SMS gateways) have strict character limits.
3. **Tracking & Analytics**: By routing traffic through a short URL, businesses can gather critical telemetry (geographic location, device types, click timestamps, referrer websites).
4. **Brand Enhancement**: Companies use branded domains (e.g., `t.co` by Twitter, `amzn.to` by Amazon) to build trust.

### High-Level System Architecture
The application uses a 3-tier architecture:
- **Presentation Layer (Controllers)**: Receives HTTP requests, validates inputs, documents paths with Swagger/OpenAPI, and sends responses.
- **Service Layer (Business Logic)**: Implements URL shortening algorithms, handles user registration, validates security tokens, queries the Redis cache, and logs events.
- **Data Access Layer (Repositories)**: Performs CRUD operations on PostgreSQL via Spring Data JPA and Hibernate.

[DIAGRAM: architecture]

### Technologies Used
- **Language**: Java 21 (providing modern language features like Record patterns, enhanced Switch statements, and virtual threads support).
- **Framework**: Spring Boot 3.5.x (autoframing dependencies, auto-configuration, integrated web server).
- **Security**: Spring Security (securing admin panels, validating JWTs, handling password hashing with BCrypt).
- **Cache**: Redis (in-memory caching of shortened URL-to-original URL mappings to achieve sub-millisecond lookups).
- **Database**: PostgreSQL (for persistent storage of users, URLs, and authentication tokens).
- **API Documentation**: Swagger/OpenAPI 3.0 (generating interactive documentation for frontend developers).
- **Containerization**: Docker & Docker Compose (providing consistent containerized environments for development and deployment).
- **Testing**: JUnit 5 & Mockito (facilitating isolated unit tests and verification of business logic).
