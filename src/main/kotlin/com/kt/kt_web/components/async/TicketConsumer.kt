package com.kt.kt_web.components.async

import shared.TicketDTO
import com.kt.kt_web.exceptions.KafkaConsumingException
import com.kt.kt_web.services.TaskService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class TicketConsumer(private val taskService: TaskService) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(TicketConsumer::class.java)
    }

    @KafkaListener(topics = ["new-ticket"], groupId = "ticket"/*, containerFactory = "ticketListener"*/)
    fun listenTicket(ticket: TicketDTO) {
        log.info("Received Message in group 'tickets': $ticket")
        try {
            taskService.processFromTicket(ticket)
        } catch (e: RuntimeException) {
            throw KafkaConsumingException("Error consumiendo el ticket", e)
        }

    }
}