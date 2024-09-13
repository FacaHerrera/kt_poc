package com.kt.kt_web.tasks

import com.kt.kt_web.repositories.TaskRepository
import com.kt.kt_web.services.TaskService
import com.kt.kt_web.util.TaskPool
import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.ToxiproxyClient
import eu.rekawek.toxiproxy.model.ToxicDirection
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.ToxiproxyContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeoutException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Disabled
class ToxiproxyTaskTest {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var taskService: TaskService

    companion object {
        private val toxiProxyNetwork: Network = Network.newNetwork()
        lateinit var postgresqlProxy: Proxy

        @Container
        private val postgres: PostgreSQLContainer<Nothing> = postgres("postgres:16.0") {
            withDatabaseName("tasks")
            withUsername("postgres")
            withPassword("postgres")
            withExposedPorts(5432)
            withNetwork(toxiProxyNetwork)
            withNetworkAliases("postgres")
        }

        @Container
        private val toxiproxy: ToxiproxyContainer = ToxiproxyContainer("ghcr.io/shopify/toxiproxy:2.5.0")
            .withNetwork(toxiProxyNetwork)
            .withNetworkAliases("toxiproxy")
            .withExposedPorts(8474, 8475) // 8474: Control port | 8475: Proxy Port
            .dependsOn(postgres)

        @JvmStatic
        @DynamicPropertySource
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            val toxiProxyHost = toxiproxy.host
            val toxiControlPort = toxiproxy.getMappedPort(8474)
            val toxiProxyPort = toxiproxy.getMappedPort(8475)

            val toxiproxyClient = ToxiproxyClient(toxiProxyHost, toxiControlPort)
            val proxiedDatasourceURL = "$toxiProxyHost:${toxiProxyPort}"
            val actualDatasourceURL = "${postgres.host}:${postgres.getMappedPort(5432)}"

            postgresqlProxy = toxiproxyClient.createProxy(
            "postgresql", proxiedDatasourceURL, actualDatasourceURL)

            val datasource = "jdbc:postgresql://$proxiedDatasourceURL/${postgres.databaseName}"

            registry.add("spring.datasource.url") { datasource }
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.flyway.enabled") { false }
            registry.add("spring.kafka.enabled") { false }
        }
    }

    @Nested
    @DisplayName("Normal")
    inner class NormalTestEntity {
        @Test
        @Rollback
        fun `should save the task normally`() {
            postgresqlProxy.toxics().latency("latency-1", ToxicDirection.DOWNSTREAM, 2000).setJitter(100)
            taskRepository.findAll()
        }
    }

    @Nested
    @DisplayName("latency")
    inner class LatencyTest {
        @Test
        @Rollback
        fun `should save the task with some latency`() {
            postgresqlProxy.toxics().latency("latency-2", ToxicDirection.DOWNSTREAM, 1600).setJitter(100)
            taskRepository.save(TaskPool.getRandom())

            assertEquals(1, taskRepository.findAll().size)
        }

        @Test
        @Rollback
        fun `should fail to save because of excessive latency`() {
            postgresqlProxy.toxics().latency("latency-3", ToxicDirection.DOWNSTREAM, 10000).setJitter(100)
            assertThrows(TimeoutException::class.java) {
                taskService.saveWithTimeout(TaskPool.getRandom())
            }
        }

        @Test
        @Rollback
        fun `should fail to retrieve data because of excessive latency`() {
            postgresqlProxy.toxics().timeout("latency-4", ToxicDirection.UPSTREAM, 10000)
            assertThrows(TimeoutException::class.java) {
                taskService.findAllWithTimeout()
            }
        }
    }

}