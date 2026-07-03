# Chapter 12: Redis Cache

## Redis Cache
Caching is the practice of storing active data in-memory to provide lightning-fast read operations.

### Why Redis?
URL shorteners are read-heavy applications (e.g., a URL might be shortened once but accessed millions of times). Querying PostgreSQL for every redirect is slow and stresses database connections. Redis enables lookups in sub-milliseconds.

### Caching Strategy in this Project
We implement a **Cache-Aside (Read-Through)** caching strategy:

[DIAGRAM: redis_cache]

1. **Client Redirect Request** hits the API.
2. **Cache Lookup**: Read key `shortCode` from Redis.
3. **Cache Hit**: Returns the long URL, triggers click tracking asynchronously, and redirects immediately.
4. **Cache Miss**: Queries PostgreSQL. If found, caches the record in Redis and redirects.

### TTL (Time-To-Live) Management
A TTL is set on cache keys to prevent stale entries. This project computes the remaining TTL dynamically:
`ttlSeconds = expiryDate - currentTime`
The key is stored in Redis with a TTL of `min(ttlSeconds, 24 Hours)`. This ensures that expired short codes are evicted from cache automatically.
