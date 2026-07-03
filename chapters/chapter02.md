# Chapter 2: Project Folder Structure

## Directory Layout and Project Structure
The structure of a Spring Boot application is organized using standard conventions to separate concerns. This project follows the package-by-feature pattern under a main package `com.piyush.Urlshortener`.

### Root Folder Contents
- `pom.xml`: The Maven Project Object Model configuration. Contains dependencies (JPA, Redis, Security, JWT, Validation, Actuator, Swagger) and build plugins.
- `Dockerfile`: Multi-stage build definitions for creating the runtime container image.
- `docker-compose.yml`: Orchestration file defining PostgreSQL database, Redis cache, and backend application services.
- `src/main/resources`: Contains configuration files and static resources.
  - `application.properties`: Configuration for local development (database credentials, Redis host, JWT secret).
  - `application-docker.properties`: Specialized profiles when running within Docker containers.

### Main Package Structure (`src/main/java/com/piyush/Urlshortener`)
1. **`config`**:
   - `OpenApiConfig.java`: Configures the OpenAPI specs and links Bearer JWT authorization inside Swagger UI.
   - `RedisConfig.java`: Initializes the `RedisTemplate` and Serializers to save keys/values as strings in Redis.
   - `SecurityConfig.java`: Configures Spring Security Filter Chains, public/protected request matchers, and enables stateless sessions.
2. **`controller`**:
   - `AuthController.java`: Exposes authentication paths like register, login, and token refresh.
   - `UrlController.java`: Houses the core endpoints for URL shortening, redirection, and statistics tracking.
   - `UserController.java`: Provides user-specific views, such as listing all URLs created by the authenticated user.
   - `AdminController.java`: Administrative endpoints protected by role-based checks.
3. **`dto`**:
   - Data Transfer Objects containing payload definitions for requests and responses. Isolates database entities from API clients.
4. **`entity`**:
   - Database tables mapped to Java classes via JPA annotations (`User`, `Url`, `RefreshToken`).
5. **`exception`**:
   - Custom exceptions (`UrlNotFoundException`, `UserAlreadyExistsException`, etc.) and the `GlobalExceptionHandler` to translate exceptions into standardized API responses.
6. **`repository`**:
   - Repository interfaces extending `JpaRepository` to perform CRUD operations on database tables.
7. **`scheduler`**:
   - `UrlCleanupScheduler.java`: A background worker designed to periodically delete expired URLs.
8. **`security`**:
   - `JwtAuthenticationFilter.java`: Middleware component executing on every request to extract, validate, and set JWT credentials in the Spring Security context.
9. **`service`**:
   - Holds the main business logic files (`UrlService`, `AuthService`, `JwtService`, `RefreshTokenService`, `UserService`, `UrlClickTracker`).
