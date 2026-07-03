# Chapter 13: Database

## PostgreSQL Database Architecture
PostgreSQL provides persistent, relational storage for the application.

### Tables and Relationships
1. **`users`**:
   - Stores account credentials.
   - Primary Key: `id` (Identity sequence).
   - Columns: `username` (unique), `email` (unique), `password`, `role`, `createdAt`.
2. **`urls`**:
   - Stores redirection records.
   - Primary Key: `id` (Identity sequence).
   - Columns: `originalUrl`, `shortCode` (unique), `clickCount`, `createdAt`, `expiryDate`, `user_id` (Foreign Key referencing `users(id)`).
3. **`refresh_token`**:
   - Stores active refresh tokens.
   - Primary Key: `id` (Identity sequence).
   - Columns: `token` (unique), `expiresAt`, `user_id` (Foreign Key referencing `users(id)`).

[DIAGRAM: database_erd]

### Database Constraints and Indexes
- **Unique Constraints**: Configured on `users(username)`, `users(email)`, `urls(shortCode)`, and `refresh_token(token)` to prevent duplicates.
- **Foreign Keys**: Cascade relationships ensure data consistency.
- **Indexes**:
  - `idx_urls_short_code` on `urls(shortCode)` speeds up database searches during redirection cache misses.
  - `idx_urls_expiry_date` on `urls(expiryDate)` optimizes the background clean-up task that deletes expired links.
