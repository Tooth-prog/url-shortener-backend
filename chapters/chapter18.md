# Chapter 18: Docker

## Containerization
Docker containerizes the application, packaging code, runtime dependencies, and configurations into a portable image that runs consistently on any server.

### Dockerfile Walkthrough
```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/Urlshortener-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```
- `FROM`: Starts from a minimal Eclipse Temurin JRE runtime image matching Java 21.
- `WORKDIR`: Sets the working directory inside the container.
- `COPY`: Copies the built JAR file from the local target directory.
- `EXPOSE`: Documents that the container will listen on port `8080`.
- `ENTRYPOINT`: Specifies the startup command.

### Docker Compose Walkthrough
`docker-compose.yml` configures three integrated containers:
1. **`postgres`**: Runs database container mapped to host port `5432`. Includes health checks.
2. **`redis`**: Runs cache container mapped to host port `6379`.
3. **`app`**: Compiles the backend container, passing database and cache coordinates, and declaring startup dependencies (`depends_on: service_healthy`) on Postgres and Redis.

[DIAGRAM: docker_compose]
