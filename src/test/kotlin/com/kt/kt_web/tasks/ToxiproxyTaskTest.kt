package com.kt.kt_web.tasks

import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.ToxiproxyClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.Network
import org.testcontainers.containers.ToxiproxyContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class ToxiproxyTaskTest {
    companion object {
        private val network: Network = Network.newNetwork()

        @Container
        private val postgres = postgres("postgres:16.0") {
            withDatabaseName("tasks")
            withUsername("root")
            withPassword("root")
            //withInitScript("sql/schema.sql")
            withExposedPorts(5432)
            withNetwork(network)
        }

        @Container
        var toxiproxy: ToxiproxyContainer = ToxiproxyContainer("ghcr.io/shopify/toxiproxy:2.5.0")
            .withNetwork(network)
            .dependsOn(postgres)

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.username", postgres::getUsername)

            val toxiproxyClient: ToxiproxyClient = ToxiproxyClient(toxiproxy.host, toxiproxy.controlPort)
            val proxy: Proxy = toxiproxyClient.createProxy("postgresql", "0.0.0.0:8666", "postgres:5432")
        }
    }

    private lateinit var proxy: ToxiproxyContainer

}