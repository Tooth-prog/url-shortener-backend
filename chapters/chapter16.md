# Chapter 16: Swagger

## Swagger Documentation
APIs must be clearly documented to let frontend teams integrate them easily. Swagger parses API paths and generates interactive web pages.

### Swagger Config and Schema Annotations
- **`OpenApiConfig`**: Registers Swagger schemas and defines the Bearer token authorization setup.
- **`@Tag`**: Categorizes routes (e.g., "URL Management").
- **`@Operation`**: Provides summaries and descriptions for paths.
- **`@Schema`**: Defines documentation info and example values on DTO fields.

### Accessing Swagger UI
When running locally, Swagger UI is available at:
`http://localhost:8080/swagger-ui.html`
It provides an interactive console to test endpoints, attach authorization tokens, and review request/response schemas.
