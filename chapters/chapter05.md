# Chapter 5: Controllers

## REST Controller Configurations
Controllers are the entry points to the web service. They define HTTP routes, consume request formats, and produce JSON structures.

### 1. `AuthController`
- **Register User (`POST /api/auth/register`)**:
  - **Purpose**: Creates a new user profile.
  - **Payload**: `RegisterRequest` (username, email, password).
  - **Response**: `UserResponse` (id, username, email, role).
  - **Status Code**: 200 OK (or 409 Conflict if email/username already exists).
- **Login User (`POST /api/auth/login`)**:
  - **Purpose**: Validates user credentials and issues session tokens.
  - **Payload**: `LoginRequest` (email, password).
  - **Response**: `ApiResponse<LoginResponse>` (containing access token and refresh token).
  - **Status Code**: 200 OK (or 401 Unauthorized if incorrect credentials).
- **Refresh Token (`POST /api/auth/refresh`)**:
  - **Purpose**: Generates a new short-lived access token using a long-lived refresh token.
  - **Payload**: `RefreshRequest` (refreshToken).
  - **Response**: `ApiResponse<LoginResponse>` (new access token, same refresh token).
  - **Status Code**: 200 OK (or 401/404 if refresh token is expired or invalid).

### 2. `UrlController`
- **Shorten URL (`POST /api/v1/shorten`)**:
  - **Purpose**: Shortens a long URL. Can accept optional custom codes and custom expiry durations.
  - **Payload**: `UrlRequest` (url, customCode, expiryDays).
  - **Response**: `UrlResponse` (shortCode, shortUrl, expiryDate).
- **Redirect (`GET /api/v1/{shortCode}`)**:
  - **Purpose**: Resolves a short code and redirects the client browser to the original URL.
  - **Response**: HTTP 302 Found with the `Location` header set to the original URL.
- **Analytics (`GET /api/v1/analytics/{shortCode}`)** & **Stats (`GET /api/v1/stats/{code}`)**:
  - **Purpose**: Retrieves details (original URL, click counts, creation date, expiry date).
  - **Response**: `UrlStatsResponse`.

### 3. `UserController`
- **List User URLs (`GET /api/user/urls`)**:
  - **Purpose**: Lists all URLs created by the currently authenticated user.
  - **Response**: `List<UserUrlResponse>`.

### 4. `AdminController`
- **List Users (`GET /api/admin/users`)**:
  - **Purpose**: Lists all registered user accounts. Restricted to administrators.
  - **Response**: `List<UserResponse>`.
