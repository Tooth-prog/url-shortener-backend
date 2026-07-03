# Chapter 14: Exception Handling

## Exception Handling
Robust applications intercept errors gracefully and respond with descriptive, structured JSON messages.

### The GlobalExceptionHandler Pattern
Spring Boot utilizes `@RestControllerAdvice` to build interceptors that catch exceptions thrown anywhere in the application.

### Custom Exceptions in this Project
- `UserNotFoundException`: HTTP 404 Not Found.
- `UrlNotFoundException`: HTTP 404 Not Found.
- `InvalidCredentialsException`: HTTP 401 Unauthorized.
- `UserAlreadyExistsException`: HTTP 409 Conflict.
- `ShortCodeAlreadyExistsException`: HTTP 409 Conflict.
- `UrlExpiredException`: HTTP 410 Gone.

### Validation Exception Handling
If request data validations fail (e.g., an invalid email format), Spring throws `MethodArgumentNotValidException`. `GlobalExceptionHandler` intercepts this, extracts the validation error mappings (e.g., field: error message), and returns them with an HTTP 400 Bad Request status.

### Error Response Envelope
```json
{
  "success": false,
  "message": "Detailed error message",
  "data": null
}
```
