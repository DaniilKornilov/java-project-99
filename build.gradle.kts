plugins {
    id("application")
    id("checkstyle")
    id("jacoco")
    id("com.github.ben-manes.versions") version "0.53.0"
    id("org.sonarqube") version "7.0.1.6134"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.sentry.jvm.gradle") version "5.12.2"
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

sentry {
    includeSourceContext = true

    org = "daniilkornilov"
    projectName = "java-spring-boot"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    dependencies {
        dependency("org.mapstruct:mapstruct:1.6.3")
        dependency("org.mapstruct:mapstruct-processor:1.6.3")
        dependency("org.projectlombok:lombok-mapstruct-binding:0.2.0")
        dependency("io.jsonwebtoken:jjwt-api:0.13.0")
        dependency("io.jsonwebtoken:jjwt-impl:0.13.0")
        dependency("io.jsonwebtoken:jjwt-jackson:0.13.0")
        dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
        dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.20.1")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.mapstruct:mapstruct")
    implementation("io.jsonwebtoken:jjwt-api")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.mapstruct:mapstruct")
    testRuntimeOnly("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    testRuntimeOnly("io.jsonwebtoken:jjwt-impl")
    testRuntimeOnly("io.jsonwebtoken:jjwt-jackson")
    testRuntimeOnly("io.jsonwebtoken:jjwt-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.matching { it.name.startsWith("sentry") }.configureEach {
    onlyIf {
        !System.getenv("SENTRY_AUTH_TOKEN").isNullOrBlank()
    }
}
