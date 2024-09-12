package com.kt.kt_web.exceptions

class KafkaConsumingException(
    message: String?,
    cause: Throwable? = null
) : KafkaException("Consuming Error: $message", cause)