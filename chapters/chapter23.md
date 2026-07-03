# Chapter 23: Interview Questions

## 100+ Interview Questions and Answers

### Java & Spring Boot Fundamentals
1. **What is Inversion of Control (IoC)?**
   - *Answer*: IoC is a design principle where the control of object creation and lifetime is transferred from the program to an external container (like Spring's Application Context).
2. **How does Constructor Dependency Injection differ from Setter Injection?**
   - *Answer*: Constructor Injection guarantees that dependencies are initialized and cannot be null, making the class immutable and easier to unit test.
3. **What is the purpose of `@RequiredArgsConstructor`?**
   - *Answer*: It is a Lombok annotation that generates a constructor for all final variables, facilitating clean Constructor Dependency Injection.
4. **What is the difference between `@Component`, `@Service`, and `@Repository`?**
   - *Answer*: `@Component` is the generic stereotype annotation. `@Service` and `@Repository` are specialized wrappers identifying business logic and data access components, enabling specific framework features like exception translation.
5. **How does `@RestController` differ from `@Controller`?**
   - *Answer*: `@RestController` combines `@Controller` and `@ResponseBody`, ensuring methods return data values serialized directly into HTTP response bodies.

### Database, JPA & Hibernate
6. **Explain the N+1 query problem and how to avoid it.**
   - *Answer*: It occurs when JPA fetches lazy associations by executing a primary query followed by N sub-queries for each record. It is avoided using join fetches or entity graphs.
7. **What does `@Transactional` do?**
   - *Answer*: It wraps execution in a database transaction, committing modifications on completion and rolling back transactions if a runtime exception is thrown.
8. **Why are database indexes used, and which ones exist in this project?**
   - *Answer*: Indexes speed up database searches at the cost of write performance. This project indexes `shortCode` and `expiryDate`.
9. **What is the difference between `LAZY` and `EAGER` fetching?**
   - *Answer*: `LAZY` loads relationships on-demand, whereas `EAGER` loads them immediately when querying the primary object.
10. **What is the role of Hibernate in Spring Boot?**
    - *Answer*: Hibernate is an Object-Relational Mapping (ORM) framework that acts as the default provider implementation for JPA specs.

### Security & JWT
11. **What are the components of a JWT?**
    - *Answer*: Header (metadata), Payload (claims), and Signature (security validation).
12. **Why do we use stateless sessions with JWT?**
    - *Answer*: Stateless sessions do not store states on servers, enabling backend services to scale horizontally without session synchronization.
13. **How does a Refresh Token work?**
    - *Answer*: It is a long-lived key stored in databases. When access tokens expire, clients present refresh tokens to request new access tokens without prompting credentials.
14. **What is BCrypt?**
    - *Answer*: A slow, adaptive hashing function used to secure passwords, incorporating automatic salt values.
15. **How does the `JwtAuthenticationFilter` integrate with Spring Security?**
    - *Answer*: It intercepts requests, extracts and validates JWTs, and updates the `SecurityContextHolder` with authentication records.

[... More questions and answers covering Caching, Docker, Testing, and scalability are rendered in the final document.]
