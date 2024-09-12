package com.kt.kt_web.exceptions

open class KafkaException(
    message: String,
    cause: Throwable? = null
) : RuntimeException("Kafka: $message", cause)
