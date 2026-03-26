# Stage 1: Build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/bookmark-0.0.1-SNAPSHOT.jar app.jar
# Render will provide the PORT env var, and our application.properties is set up to use it
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
