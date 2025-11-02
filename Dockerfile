FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY app/gradlew app/*.gradle.kts app/settings.gradle.kts ./
COPY app/gradle ./gradle
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY app/ .

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
