# Chapter 11: Spring Security

## Spring Security
Spring Security is a flexible framework that handles authentication, authorization, and protection against common attacks.

### Security Filter Chain
All incoming HTTP requests pass through a stack of filters.
- **CSRF**: Disabled since the API is stateless (clients use JWT tokens instead of cookies).
- **Session Policy**: Set to `STATELESS` (Spring Security will not create HTTP sessions).
- **Request Rules (`authorizeHttpRequests`)**:
  - `/api/auth/**`, Swagger UI, and Actuator are public (`permitAll()`).
  - Core URL redirection `GET /api/v1/{shortCode}` and anonymous shortening are public.
  - `/api/admin/**` routes are restricted to the `ADMIN` role.
  - All other routes require authentication.

[DIAGRAM: security_flow]

### JWT Filter Implementation
`JwtAuthenticationFilter` intercepts requests, extracts the JWT, and calls `JwtService` to validate it.
If valid, it constructs a `UsernamePasswordAuthenticationToken` and updates the `SecurityContextHolder`.

```
SecurityContextHolder -> SecurityContext -> Authentication (Principal, Credentials, Authorities)
```
Once the Security Context contains a valid `Authentication` object, Spring Security permits the request to pass to the Controller.
