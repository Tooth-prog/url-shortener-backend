# Chapter 7: Entities

## Persistent Entity Definitions
Entities are Java classes mapped directly to database tables. In JPA, these classes are declared using annotations to define mapping columns, indexes, and relationships.

### JPA Annotations Explained
- **`@Entity`**: Marks the Java class as a JPA database entity.
- **`@Table`**: Defines the name of the database table and structural settings (like database indexes).
- **`@Id`**: Specifies the primary key of the entity.
- **`@GeneratedValue`**: Defines the generation strategy for primary keys. `GenerationType.IDENTITY` uses PostgreSQL sequence columns.
- **`@Column`**: Maps a field to a specific database column, outlining settings like nullability, unique constraints, and character lengths.
- **`@ManyToOne` & `@OneToMany`**: Declares standard table relationships.
- **`@Enumerated`**: Maps Java enums as strings or integers. `EnumType.STRING` saves enum names directly (e.g., 'USER', 'ADMIN').

### Database Schema Mappings
1. **`User` (Table: `users`)**:
   - Fields: `id` (PK), `username` (unique), `email` (unique), `password` (hashed), `role` (enum String), `createdAt`.
   - Relationships: `@OneToMany(mappedBy = "user")` - maps one user to many shortened URLs.
2. **`Url` (Table: `urls`)**:
   - Fields: `id` (PK), `originalUrl` (length 2048), `shortCode` (unique, indexed), `clickCount` (default 0), `createdAt`, `expiryDate` (indexed), `user_id` (FK).
   - Indexes:
     - `idx_urls_short_code` on `shortCode` (for rapid lookups).
     - `idx_urls_expiry_date` on `expiryDate` (for cleaner cleanup query execution).
3. **`RefreshToken` (Table: `refresh_token`)**:
   - Fields: `id` (PK), `token` (unique), `expiresAt`, `user_id` (FK).
