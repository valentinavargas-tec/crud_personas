# Build stage
FROM gradle:8.12-jdk21 AS builder

WORKDIR /workspace

# Copy both modules for multi-project compilation1
COPY comun ./comun
COPY microservicio ./microservicio

# Build the project
WORKDIR /workspace/microservicio
#nuevo
RUN gradle bootJar -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Expose microservice port (8083)
EXPOSE 8083

# Copy the executable jar from builder stage
COPY --from=builder /workspace/microservicio/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
