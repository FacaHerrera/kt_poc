package com.kt.kt_web.entities.model

import com.kt.kt_web.entities.dto.TaskDTO
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Task(
    public override var id: Long?,

    @Column(name = "title")
    var title: String?,

    @Column(name = "description")
    var description: String?,

    /*@OneToMany(mappedBy = State_.TASK, cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var stateRecord: MutableList<State>?*/

): ModelEntity() {
    constructor() : this(
        null,
        null,
        null,
        /*mutableListOf(
            State(LocalDateTime.now(), null, StateOption.TO_DO, null)
        )*/
    )

    constructor(title: String?, description: String?/*, stateRecord: MutableList<State>?*/) : this(
        null,
        title,
        description,
        //stateRecord
    )

    /*fun addState(state: State) {
        stateRecord?.add(state)
    }*/

    fun toDTO(states: List<State>? = null): TaskDTO {
        val statesDTO = states?.map { it.toDTO() }?.toMutableList()
        return TaskDTO(id, title, description, statesDTO)
    }
}