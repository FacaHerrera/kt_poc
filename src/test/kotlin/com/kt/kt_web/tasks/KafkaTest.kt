package com.kt.kt_web.tasks

import com.kt.kt_web.entities.model.Task
import java.util.concurrent.TimeUnit.SECONDS
import com.kt.kt_web.repositories.TaskRepository
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import shared.TicketDTO
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class KafkaTest {
    companion object {
        @Container
        private val postgres = postgres("postgres:16.0") {
            withDatabaseName("tasks")
            withUsername("root")
            withPassword("root")
            //withInitScript("sql/schema.sql")
        }

        @Container
        private val kafka: KafkaContainer = KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
        )

        @JvmStatic
        @DynamicPropertySource
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.username", postgres::getUsername)

            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
        }
    }

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, TicketDTO>

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Nested
    @DisplayName("Kafka Ticket Flux")
    inner class TicketFluxTests {
        @Test
        fun `should persist consumed kafka ticket`() {
            val ticketDTO = TicketDTO(
                "Kafka Ticket",
                "Kafka Integration Test",
                "fherrera",
                LocalDateTime.now()
            )

            kafkaTemplate.send("new-ticket", ticketDTO.getKey(), ticketDTO)

            await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted {
                    val task: Optional<Task> = taskRepository.findByTitle("Kafka Ticket")

                    assertNotNull(task)
                    assertThat(task.get().title).isEqualTo("Kafka Ticket")
                }
        }
    }
}