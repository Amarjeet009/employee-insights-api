# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim



# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/employee-insights-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the application
EXPOSE 8084

# Allow profile to be passed via environment variable
ENV SPRING_PROFILES_ACTIVE=dev

# Run the application with the selected profile
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]




