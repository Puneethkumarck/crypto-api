# Step 1: Build the application using Gradle
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy the Gradle wrapper and settings files
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY app/src/ app/src/

# Grant execution permission to the Gradle wrapper
RUN chmod +x gradlew

# Build the project
RUN ./gradlew clean build --no-daemon

# Step 2: Use the JRE image for runtime
FROM eclipse-temurin:21-jre

# Copy the built JAR from the build stage
COPY --from=build /app/app/build/libs/app.jar /opt/app.jar

# Copy the entrypoint script
COPY ./deploy/docker/entrypoint.sh /opt/entrypoint.sh

# Grant execution permission to the entrypoint script
RUN chmod +x /opt/entrypoint.sh

# Expose port 8080
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["/opt/entrypoint.sh"]
