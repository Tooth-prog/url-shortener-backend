# Chapter 21: Design Patterns

## Software Design Patterns
This project leverages structural and creational design patterns.

### 1. Repository Pattern
Decouples domain business logic from physical data storage mechanisms. By interacting with the `UrlRepository` interface instead of writing raw SQL statements, the application can switch database technologies without modifications to the service layer.

### 2. Builder Pattern (Lombok `@Builder`)
Enables constructing complex objects incrementally. Avoids writing multiple constructors and makes code easier to read.
`Url.builder().originalUrl("...").shortCode("...").build()`

### 3. Dependency Injection
Enables weak coupling between components, delegating dependency instantiation to the Spring Framework.

### 4. Proxy Pattern (AOP)
Spring implements `@Async` and `@Transactional` annotations by wrapping target classes in proxy objects. When a class invokes a transactional method, the proxy opens a database transaction, routes the call to the service method, and commits or rolls back based on execution results.
