# Chapter 17: Actuator

## Actuator
Spring Boot Actuator exposes operational endpoints to monitor application health and collect telemetry.

### Production Monitoring
This project exposes actuator metrics in `application.properties`:
`management.endpoints.web.exposure.include=health,info,metrics`

Key endpoints:
- `/actuator/health`: Returns service health status. Companies configure load-balancers to hit this path to determine if a container is healthy.
- `/actuator/metrics`: Provides insights into memory allocations, JVM usage, garbage collection, active database connection counts, and HTTP requests.
- `/actuator/info`: Displays basic application details.
