# Chapter 8: Repositories

## Repositories
Repositories are interfaces that map database access methods. Spring Data JPA auto-generates SQL queries based on custom method declarations.

### 1. `UserRepository`
- `findByEmail(String email)`: Generates: `SELECT * FROM users WHERE email = ?`
- `findByUsername(String username)`: Generates: `SELECT * FROM users WHERE username = ?`

### 2. `RefreshTokenRepository`
- `findByToken(String token)`: Fetches a refresh token object.
- `deleteByUserId(Long userId)`: Cleans up old user tokens during login to avoid double-token entries. Annotated with `@Modifying` and `@Transactional` to execute write queries.

### 3. `UrlRepository`
- `findByShortCode(String shortCode)`: Lookups URL entity by code.
- `findByOriginalUrl(String originalUrl)`: Checks if a long URL was already shortened, enabling caching optimization.
- `deleteByExpiryDateBefore(LocalDateTime now)`: Bulk deletes expired links.
- `incrementClickCountByShortCode(String shortCode)`:
  - Custom JPQL query: `UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode`
  - Highly performant write query mapped via `@Modifying` and `@Transactional`.

### JpaRepository and Spring Data JPA Under the Hood
JPA Repositories abstract boilerplate database interactions. They offer standard CRUD methods (`save`, `findById`, `findAll`, `delete`) out-of-the-box. When Java calls `urlRepository.save(url)`, Hibernate checks if the entity has an ID. If it does not, it generates an `INSERT` statement; otherwise, it triggers a database `UPDATE` statement.
