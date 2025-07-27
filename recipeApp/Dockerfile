LABEL authors="Santam"

# Use a Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the project jar file (replace with your actual jar name during build)
COPY target/recipe-app-0.0.1-SNAPSHOT.jar app.jar

# Expose port (should match your server.port in application.properties)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
