package com.kt.kt_web.configuration

import com.kt.kt_web.tasks.postgres
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    fun postgres(): PostgreSQLContainer<Nothing> = postgres("postgres:16.0") {
        withDatabaseName("tasks")
        withUsername("root")
        withPassword("root")
    }

    @Bean
    @ServiceConnection
    fun kafka(): KafkaContainer = KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    )
}