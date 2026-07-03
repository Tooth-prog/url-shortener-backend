# Chapter 15: Logging

## Logging Architecture
Logging provides visibility into system states during development, debugging, and production monitoring.

### SLF4J and Logback
This project uses SLF4J (Simple Logging Facade for Java) as a wrapper interface, letting Spring Boot configure the runtime engine (Logback) under the hood.

### Logging Levels
1. **`TRACE`**: Highly detailed, step-by-step logs (disabled by default).
2. **`DEBUG`**: Diagnostic logs useful during debugging.
3. **`INFO`**: High-level events showing execution flows (e.g., "URL saved successfully").
4. **`WARN`**: Warnings for non-critical issues (e.g., "JWT validation failed: expired").
5. **`ERROR`**: Actionable errors indicating failures (e.g., "Unexpected Exception").

### Best Practices in this Codebase
- Avoid using `System.out.println()` because it blocks input/output operations, slowing down responses.
- Uses dynamic logging placeholders `log.info("Creating short URL for: {}", url)` to avoid memory allocations for string concatenations when log levels are disabled.
- Standardizes log patterns for critical events: shortening requests, cache hits, database queries, and exception routes.
