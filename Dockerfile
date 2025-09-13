# --- STAGE 1: Build the application ---
# Use an official OpenJDK 17 image that includes Maven for building
FROM maven:3.9.6-eclipse-temurin-17-focal AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file first to leverage Docker's layer caching
COPY pom.xml .

# Copy the rest of the application's source code
COPY src ./src

# Run the Maven command to build the application into a JAR file
# The -DskipTests flag speeds up the build process for deployment
RUN mvn package -DskipTests


# --- STAGE 2: Run the application ---
# Use a lightweight, secure base image with just the Java Runtime Environment
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the JAR file that was created in the 'build' stage
COPY --from=build /app/target/project-showcase-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that our Spring Boot application runs on
EXPOSE 8080

# The command that will be executed when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]