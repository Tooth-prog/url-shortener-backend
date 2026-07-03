# Chapter 4: Project Flow

## Request Lifecycle Flow
Every API call passes through a structured chain of components. Let's trace the flow from the client to the database and back.

### The Request Flow Sequence
1. **Client Request**: A client sends an HTTP Request (e.g., `GET /api/v1/abc123`).
2. **Tomcat Servlet Container**: The servlet container listens on port `8080` and receives the raw socket stream, parsing it into an `HttpServletRequest` object.
3. **Filters (Spring Security)**:
   - The request hits the security filter chain.
   - `JwtAuthenticationFilter` intercepts the request, reads the `Authorization` header, extracts the JWT, validates it via `JwtService`, and populates the `SecurityContextHolder`.
4. **DispatcherServlet**: The central gateway servlet that routes requests to appropriate controllers.
5. **HandlerMapping**: Finds the controller class and method matching the URI path `/api/v1/{shortCode}`.
6. **Controller Layer (`UrlController`)**:
   - Invokes the method matching the request mapping.
   - Calls `UrlService` to retrieve the original URL.
7. **Service Layer (`UrlService`)**:
   - Executes cache check against Redis.
   - On cache miss: Queries the `UrlRepository`.
   - Schedules asynchronous click-count increment.
8. **Repository Layer (`UrlRepository`)**: Translates Java database requests into SQL queries.
9. **Persistence Layer (Hibernate / JPA)**: Converts Java database calls into SQL statements and executes them against PostgreSQL.
10. **Database (PostgreSQL)**: Returns the matching URL record.
11. **Response Pipeline**: The service returns the entity, the controller formats the response (e.g., HTTP 302 redirect), and the web server returns the HTTP response to the client.

[DIAGRAM: request_lifecycle]
