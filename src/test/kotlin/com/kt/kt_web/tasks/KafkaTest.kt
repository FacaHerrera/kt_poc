package com.kt.kt_web.tasks

import com.kt.kt_web.configuration.TestcontainersConfiguration
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
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.testcontainers.junit.jupiter.Testcontainers
import shared.TicketDTO
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class)
@Testcontainers
class KafkaTest {

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