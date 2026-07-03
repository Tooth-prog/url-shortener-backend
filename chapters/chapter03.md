# Chapter 3: Spring Boot Fundamentals Used

## Core Concepts and Annotations
To understand Spring Boot, one must grasp its main building blocks. Let's break down the annotations and design patterns used in this codebase.

### Spring IoC (Inversion of Control) and DI (Dependency Injection)
- **IoC Container**: Instead of the developer creating Java classes manually using the `new` keyword, the Spring Framework instantiates and manages the lifetime of components (Beans).
- **Dependency Injection**: Injecting dependencies (classes that a class depends on) automatically during runtime.

### Essential Annotations
1. **`@Component`**: The base annotation indicating a class is a Spring-managed Bean. Spring will scan and register this bean in the Application Context.
2. **`@Service`**: A specialization of `@Component` that signifies the class contains business logic.
3. **`@Repository`**: A specialization of `@Component` that indicates the class deals with database access. It also enables automatic exception translation.
4. **`@RestController`**: Combines `@Controller` and `@ResponseBody`. It tells Spring that methods return JSON payloads directly, rather than HTML views.
5. **`@Configuration`**: Marks a class as a source of bean definitions. Spring will process the class to generate bean configurations.
6. **`@Bean`**: Used on methods inside `@Configuration` classes. Tells Spring that the returned object must be registered as a singleton bean in the application context.
7. **`@Autowired`**: Instructs Spring to inject a dependency. In modern Spring Boot, constructor injection is preferred over `@Autowired` on fields to make tests easier to write.
8. **`@RequiredArgsConstructor`**: A Lombok annotation that automatically generates a constructor for final fields. This is the modern, clean way to implement Constructor Dependency Injection.

### Request Routing Annotations
- **`@RequestMapping`**: Defines the base URI path mapped to a controller class.
- **`@PostMapping`**: Handles HTTP POST requests. Commonly used for write/save actions.
- **`@GetMapping`**: Handles HTTP GET requests. Commonly used for read-only query actions.
- **`@PathVariable`**: Binds a dynamic URI path parameter (e.g., `{shortCode}`) to a Java method parameter.
- **`@RequestBody`**: Deserializes incoming JSON payloads into Java DTO objects.

### Cross-cutting Concerns
- **`@Valid`**: Triggers validations on incoming request body models (e.g., checking if email is formatted correctly, password is of minimum length).
- **`@Transactional`**: Manages database transactions. If a method throws a runtime exception, the transaction is automatically rolled back, protecting database consistency.
