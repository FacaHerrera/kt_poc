package com.kt.kt_web.configuration

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonSerializer
import shared.TicketDTO

@TestConfiguration
@EnableKafka
class KafkaTestConfiguration {
    @Bean
    fun producerFactory(): ProducerFactory<String, TicketDTO> {
        val configProps = mapOf<String, Any>(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, TicketDTO> {
        return KafkaTemplate(producerFactory())
    }
}