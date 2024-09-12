package com.kt.kt_web.entities.dto

import com.kt.kt_web.entities.model.State
import com.kt.kt_web.entities.model.StateOption
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class StateDTO(
    var id: Long?,

    var dateFrom: LocalDateTime?,

    var dateTo: LocalDateTime?,

    @NotNull
    var state: String,
) {
    fun toEntity(): State = State(dateFrom, dateTo, StateOption.valueOf(state), null)
}