# Chapter 6: DTOs

## Data Transfer Objects (DTOs)
In modern API architecture, database structures should never be exposed directly to API clients. DTOs serve as a contract layer between the internal database schemas and external consumers.

### Why Use DTOs?
1. **Decoupling**: Prevents client contracts from breaking when database tables are altered.
2. **Security**: Avoids over-sharing data. For example, the `User` entity has a hashed `password` field. Returning a `User` entity directly in a response could leak password hashes, whereas returning a `UserResponse` DTO ensures only safe fields (ID, username, email) are sent.
3. **Optimized Payloads**: Transports only fields required by the UI client.
4. **Validation Isolation**: Allows defining input validation constraints (like `@NotBlank`, `@Size`) directly on the request models.

### Field Explanations
- `RegisterRequest`: Contains validation annotations (`@NotBlank`, `@Email`, `@Size`).
- `UrlRequest`: Validates that URL starts with http/https and matches structure patterns using `@URL` and `@Pattern`.
- `UrlResponse`: Returns the generated `shortCode`, `shortUrl` (resolved with domain prefix), and `expiryDate`.
- `ApiResponse<T>`: A generic envelope structure wrapping API payloads to provide a uniform structure: `{ "success": true, "message": "...", "data": T }`.
