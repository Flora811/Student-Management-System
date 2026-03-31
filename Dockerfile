# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the package
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# The JAR name matches your artifactId and version in pom.xml
COPY --from=build /app/target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]