package com.kt.kt_web.tasks

import com.kt.kt_web.configuration.TestcontainersConfiguration
import com.kt.kt_web.entities.model.Task
import com.kt.kt_web.repositories.TaskRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@Sql(scripts = ["classpath:/sql/init-data.sql"])
@DataJpaTest
@Import(TestcontainersConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var postgres: PostgreSQLContainer<Nothing>

    @Nested
    @DisplayName("Connection to Containers Tests")
    inner class ConnectionTest {
        @Test
        fun `should establish connection with postgres container`() {
            assertThat(postgres.isCreated).isTrue()
            assertThat(postgres.isRunning).isTrue()
        }
    }

    @Nested
    @DisplayName("Tasks Repository Interactions Tests")
    inner class RepositoryTests {
        @Test
        fun `should find task by title`() {
            val foundTask = taskRepository.findByTitle("Catering Setup")
            assertNotNull(foundTask)
            assertEquals("Setting up the catering equipment for the event", foundTask.get().description)
        }

        @Test
        fun `should successfully update task`() {
            val foundTask = taskRepository.findByTitle("Catering Setup")

            if(foundTask.isPresent) {
                val task = foundTask.get()
                task.title = "Altered Catering Setup"
                taskRepository.save(task)
            }

            val updatedTask = taskRepository.findByTitle("Altered Catering Setup")

            assertNotNull(updatedTask)
            assertEquals(updatedTask.get().title, "Altered Catering Setup")
        }

        @Test
        fun `should successfully insert a task`() {
            val task = Task(null, "Rollback Task", "This transaction should be rollabacked")
            taskRepository.save(task)
            assertEquals(11, taskRepository.findAll().size)
        }

        @Test
        fun `should have initial amount of records`() {
            assertEquals(10, taskRepository.findAll().size)
        }
    }
}