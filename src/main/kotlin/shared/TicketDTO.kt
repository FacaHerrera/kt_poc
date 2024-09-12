package shared

import java.time.LocalDateTime

data class TicketDTO(val title: String, val description: String, val createdBy: String, val createdAt: LocalDateTime) {
    fun getKey(): String = "${title}${createdAt}${createdBy}"
}