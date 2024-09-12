package com.kt.kt_web.entities.dto

import com.kt.kt_web.entities.model.Task
import jakarta.validation.constraints.NotEmpty

data class TaskDTO(
    var id: Long?,

    @NotEmpty
    var title: String?,

    @NotEmpty
    var description: String?,

    var stateRecord: MutableList<StateDTO>?
) {
    fun toEntity(): Task = Task(id, title, description/*, stateRecord?.map { it.toEntity() }?.toMutableList()*/)
}