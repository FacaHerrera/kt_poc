package com.kt.kt_web.entities.model

import com.kt.kt_web.entities.dto.StateDTO
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class State(
    @Column(name = "date_from")
    var dateFrom: LocalDateTime?,

    @Column(name = "date_to")
    var dateTo: LocalDateTime?,

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    var state: StateOption?,

    @ManyToOne
    @JoinColumn(name = "task_id")
    var task: Task?

): ModelEntity() {
    constructor() : this(
        null,
        null,
        null,
        null
        /*mutableListOf(
            State(LocalDateTime.now(), null, StateOption.TO_DO, null)
        )*/
    )

    fun toDTO(): StateDTO = StateDTO(id, dateFrom, dateTo, state?.name ?: "")
}