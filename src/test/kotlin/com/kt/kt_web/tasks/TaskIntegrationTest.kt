package com.kt.kt_web.tasks

import com.kt.kt_web.configuration.TestcontainersConfiguration
import com.kt.kt_web.entities.dto.StateDTO
import com.kt.kt_web.entities.model.State
import com.kt.kt_web.entities.model.StateOption
import com.kt.kt_web.entities.model.Task
import com.kt.kt_web.repositories.StateRepository
import com.kt.kt_web.repositories.TaskRepository
import com.kt.kt_web.util.TaskPool
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.RestAssured.requestSpecification
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(TestcontainersConfiguration::class)
class TaskIntegrationTest {
    val logger: Logger = LoggerFactory.getLogger(TaskIntegrationTest::class.java)
    val baseUrl = "/api/v1/tasks"

    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var postgres: PostgreSQLContainer<Nothing>

    @Autowired
    private lateinit var stateRepository: StateRepository

    @BeforeEach
    fun setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        requestSpecification = RequestSpecBuilder()
            .setPort(port)
            .addHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
            )
            .build()

        TaskPool.getTasks().forEach {
            val savedTask = taskRepository.save(it)
            val state = State(LocalDateTime.now(), null, StateOption.TO_DO, savedTask)
            stateRepository.save(state)
        }
    }

    @AfterEach
    fun destroyDB() {
        taskRepository.deleteAll()
    }

    @Nested
    @DisplayName("Connection & Health")
    inner class ConnectionTest {
        @Test
        fun `should establish connection with postgres container`() {
            Assertions.assertThat(postgres.isCreated).isTrue()
            Assertions.assertThat(postgres.isRunning).isTrue()
        }

        @Test
        fun `should be healthy`() {
            given(requestSpecification)
                .`when`()
                    .get("/actuator/health")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .log().ifValidationFails(LogDetail.ALL)
        }
    }

    @Nested
    @DisplayName("GET Tasks")
    inner class GetTaskTests {
        @Test
        fun `should return all tasks`() {
            given(requestSpecification)
                .contentType(ContentType.JSON)
                .`when`()
                    .get(baseUrl)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(".", hasSize<Int>(5))
        }

        @Test
        fun `should obtain task by title`() {
            given(requestSpecification)
                .contentType(ContentType.JSON)
                .queryParam("title", "Catering Setup")
                .`when`()
                    .get("$baseUrl/title")
                .then()
                    .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("Catering Setup"))
        }

        @Test
        fun `should throw not found when looking for task by title`() {
            given(requestSpecification)
                .contentType(ContentType.JSON)
                .queryParam("title", "This task does not exists")
                .`when`()
                    .get("$baseUrl/title")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
        }
    }

    @Nested
    @DisplayName("POST Tasks")
    //@TestInstance(Lifecycle.PER_CLASS)
    inner class PostTaskTests {
        @Test
        fun `should save a new task`() {
            val task = Task(
                "New Task",
                "This is a new Task",
                /*mutableListOf()*/
            )

            val state = State(LocalDateTime.now(), null, StateOption.TO_DO, task)
            //task.addState(state)

            given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(task.toDTO())
                .`when`()
                    .post(baseUrl)
                .then()
                    .statusCode(HttpStatus.OK.value())

            given(requestSpecification)
                .contentType(ContentType.JSON)
                .queryParam("title", task.title)
                .`when`()
                    .get("$baseUrl/title")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("title", equalTo(task.title))
        }
    }

    @Nested
    @DisplayName("PUT Tasks")
    //@TestInstance(Lifecycle.PER_CLASS)
    inner class PutTaskTests {

    }

    @Nested
    @DisplayName("PATCH Tasks")
    //@TestInstance(Lifecycle.PER_CLASS)
    inner class PatchTaskTests {
        @Test
        fun `should add a IN_PROGRESS state to random task`() {
            val task = taskRepository.findByTitle(TaskPool.getRandom().title!!).get()

            given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(StateDTO(null, LocalDateTime.now(), null, "IN_PROGRESS"))
                .`when`()
                    .patch("$baseUrl/${task.id}/state")
                .then()
                    .statusCode(HttpStatus.OK.value())

            given(requestSpecification)
                .contentType(ContentType.JSON)
                .`when`()
                    .get("$baseUrl/${task.id}/states")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(".", hasSize<Int>(2))
                    .body("[1].state", equalTo(StateOption.IN_PROGRESS.name))
        }
    }

    @Nested
    @DisplayName("DELETE Tasks")
    //@TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteTaskTests {

    }
}