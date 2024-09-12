package com.kt.kt_web.states

import com.kt.kt_web.configuration.TestcontainersConfiguration
import com.kt.kt_web.entities.model.State
import com.kt.kt_web.entities.model.StateOption
import com.kt.kt_web.entities.model.Task
import com.kt.kt_web.repositories.StateRepository
import com.kt.kt_web.repositories.TaskRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@Testcontainers
@DataJpaTest
@Sql(scripts = ["classpath:/sql/init-data.sql"])
@Import(TestcontainersConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StateRepositoryTest {
    @Autowired
    private lateinit var stateRepository: StateRepository

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var postgres: PostgreSQLContainer<Nothing>

    @Nested
    @DisplayName("Connection to Containers Tests")
    inner class ConnectionTest {
        @Test
        fun `should establish connection with postgres container`() {
            Assertions.assertThat(postgres.isCreated).isTrue()
            Assertions.assertThat(postgres.isRunning).isTrue()
        }
    }

    @Nested
    @DisplayName("Tasks Repository Interactions Tests")
    inner class RepositoryTests {
        @Test
        fun `should find states by task`() {
            val task = Task(
                title = "Multi State Task",
                description = "This is a task with various states"
            )

            val savedTask = taskRepository.save(task)

            val now = LocalDateTime.now()
            val toDo = State(now, now.plusDays(1), StateOption.TO_DO, savedTask)
            val inProgress = State(now.plusDays(1), now.plusDays(3), StateOption.IN_PROGRESS, savedTask)
            val needInfo = State(now.plusDays(3), now.plusDays(7), StateOption.NEED_INFORMATION, savedTask)

            stateRepository.saveAll(listOf(toDo, inProgress, needInfo))

            assertEquals(
                3,
                stateRepository.findByTaskId(savedTask.id!!).size
            )
        }
    }
}