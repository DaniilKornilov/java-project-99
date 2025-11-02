plugins {
    id("application")
    id("checkstyle")
    id("jacoco")
    id("com.github.ben-manes.versions") version "0.53.0"
    id("org.sonarqube") version "7.0.1.6134"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "app"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

springBoot {
    mainClass.set("hexlet.code.app.AppApplication")
}

checkstyle {
    toolVersion = "11.1.0"
    configFile = file("$rootDir/config/checkstyle.xml")
}

jacoco {
    toolVersion = "0.8.13"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
}

sonar {
    properties {
        property("sonar.projectKey", "DaniilKornilov_java-project-99")
        property("sonar.organization", "daniilkornilov")
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
