package com.kt.kt_web.repositories

import com.kt.kt_web.entities.model.State
import com.kt.kt_web.entities.model.Task
import org.springframework.data.jpa.repository.JpaRepository

interface StateRepository : JpaRepository<State, Long> {
    fun findByTaskId(taskId: Long): List<State>
}