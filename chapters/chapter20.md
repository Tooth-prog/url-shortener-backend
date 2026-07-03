# Chapter 20: End-to-End API Walkthrough

## API Payload Catalog
Here is a collection of request and response structures for all major API paths.

### 1. User Registration
`POST /api/auth/register`
Request:
```json
{
  "username": "coder1",
  "email": "coder@gmail.com",
  "password": "mySecurePassword"
}
```
Response (200 OK):
```json
{
  "id": 1,
  "username": "coder1",
  "email": "coder@gmail.com",
  "role": "USER"
}
```

### 2. User Login
`POST /api/auth/login`
Request:
```json
{
  "email": "coder@gmail.com",
  "password": "mySecurePassword"
}
```
Response (200 OK):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "4a719c2f-e889-49ff-95f2-95f00e26372d"
  }
}
```

### 3. Shorten URL
`POST /api/v1/shorten`
Request:
```json
{
  "url": "https://www.google.com",
  "customCode": "goog",
  "expiryDays": 10
}
```
Response (200 OK):
```json
{
  "shortCode": "goog",
  "shortUrl": "http://localhost:8080/api/v1/goog",
  "expiryDate": "2026-07-13T07:52:28"
}
```

### 4. Redirection Redirect
`GET /api/v1/goog`
Response:
- **Status**: 302 Found
- **Headers**:
  - `Location`: `https://www.google.com`
