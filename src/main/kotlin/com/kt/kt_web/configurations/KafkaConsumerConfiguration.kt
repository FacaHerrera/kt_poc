package com.kt.kt_web.configurations

import shared.TicketDTO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.listener.ContainerProperties

@EnableKafka
@Configuration
class KafkaConsumerConfiguration {

    /*@Bean
    fun consumerFactory(): ConsumerFactory<String, TicketDTO> {
        val configProps = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to "ticket",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "shared",
            JsonDeserializer.VALUE_DEFAULT_TYPE to TicketDTO::class.java.name
        )
        return DefaultKafkaConsumerFactory(configProps, StringDeserializer(), JsonDeserializer(TicketDTO::class.java))
    }

    @Bean
    fun ticketListener(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TicketDTO>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, TicketDTO>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
        return factory
    }*/
}
