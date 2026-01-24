# Stage 1: Build
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests for faster build)
RUN ./mvnw package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create uploads directory
RUN mkdir -p /app/uploads

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Copy uploads folder with existing images
COPY uploads /app/uploads

# Expose port
EXPOSE 8080

# Set environment variables
ENV FILE_UPLOAD_DIR=/app/uploads
ENV SPRING_PROFILES_ACTIVE=default

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
