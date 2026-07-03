# Chapter 9: Services

## Services
Services are the core operational components of the application. They coordinate database transactions, cache lookups, validation rules, and logging actions.

### 1. `AuthService`
- **`register`**:
  1. Checks if the requested email or username is already registered. If yes, throws `UserAlreadyExistsException`.
  2. Encodes the password using `BCryptPasswordEncoder`.
  3. Builds a `User` entity, saves it to PostgreSQL, and maps it to a `UserResponse`.
- **`login`**:
  1. Fetches user details by email.
  2. Verifies the password using `passwordEncoder.matches()`. If invalid, throws `InvalidCredentialsException`.
  3. Generates a short-lived access token (JWT) using `JwtService`.
  4. Generates/saves a refresh token via `RefreshTokenService`.

### 2. `JwtService`
- Generates access tokens containing the user's email, role, issue date, and expiry details (1 hour).
- Signs tokens using `HMAC-SHA256` and a configured secret key.
- Extracts user emails and roles from token headers.

### 3. `RefreshTokenService`
- Generates 7-day UUID refresh tokens.
- Cleans up existing tokens for the user before saving a new one to prevent database constraint issues.
- Checks refresh token expiries. If expired, deletes the token from DB and throws an exception.

### 4. `UrlClickTracker`
- Features a single method `@Async public void incrementClickCountAsync(String shortCode)`.
- The `@Async` annotation runs the DB click counter inside a separate thread pool. This is a critical production pattern, ensuring redirection API calls remain fast and do not wait for slow DB write processes to finish.

### 5. `UrlService`
- **`shortenUrl`**:
  1. If custom code is provided: validates that it does not already exist in DB.
  2. If custom code is absent: checks if the URL has already been shortened. If yes, returns it from DB (idempotence).
  3. Generates a random 6-character short code.
  4. Creates the `Url` entity, associating the logged-in user if available.
  5. Saves the URL to DB and caches the mapping in Redis with a maximum 24-hour TTL.
- **`getOriginalUrl`**:
  1. Queries Redis. On cache hit, starts async click tracking and returns a light `Url` entity directly (bypassing DB queries).
  2. On cache miss: Queries DB. If missing, throws `UrlNotFoundException`.
  3. Validates expiry date. If expired, throws `UrlExpiredException`.
  4. Triggers async click tracking.
  5. Stores the mapping in Redis to optimize future lookups.
- **`getAnalytics`**: Retrieves the database record to get active click counts and dates.

### 6. `UserService`
- Fetches the active user's details from the Security Context and queries their associated URLs.
