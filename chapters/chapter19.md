# Chapter 19: Testing

## Testing Framework
Testing verifies that modifications to the code do not break core business logic.

### JUnit and Mockito
- **JUnit 5**: The runtime testing engine.
- **Mockito**: A framework used to mock external dependencies (repositories, templates) to isolate the code being tested.
- **`@Mock`**: Declares mock instances of dependencies.
- **`@InjectMocks`**: Creates the test object and automatically injects mock dependencies into it.
- **`@ExtendWith(MockitoExtension.class)`**: Initializes Mockito integrations.

### The AAA (Arrange-Act-Assert) Pattern
- **Arrange**: Sets up test parameters, inputs, and mocks.
- **Act**: Executes the target method.
- **Assert**: Verifies output results match expectations.
