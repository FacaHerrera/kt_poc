plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.kt"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Starters

	/// Data
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.flywaydb:flyway-core")


	/// Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")

	/// Parsers
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	/// Databases
	implementation("org.postgresql:postgresql:42.7.2")
	implementation("org.flywaydb:flyway-database-postgresql")

	/// Kafka
	implementation("org.springframework.kafka:spring-kafka")

	/// Logging
	implementation("org.slf4j:slf4j-api:2.0.9")
	implementation("ch.qos.logback:logback-classic:1.4.12")

	/// Actuator
	implementation("org.springframework.boot:spring-boot-starter-actuator")


	// Testing


	/// JUnit
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.testcontainers:junit-jupiter")

	/// Rest
	testImplementation("io.rest-assured:rest-assured")

	/// TestContainers
	testImplementation("org.testcontainers:postgresql:1.20.1")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:kafka")
	testImplementation("org.awaitility:awaitility")
	testImplementation("org.testcontainers:toxiproxy:1.20.1")

	/// Spring Boot
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	/// Kafka
	testImplementation("org.springframework.kafka:spring-kafka-test")


	// Runtime

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
