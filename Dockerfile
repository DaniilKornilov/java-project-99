FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY gradlew *.gradle.kts settings.gradle.kts ./
COPY /gradle ./gradle
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY / .

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
