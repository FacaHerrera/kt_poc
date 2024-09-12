package com.kt.kt_web.repositories

import com.kt.kt_web.entities.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findByTitle(title: String): Optional<Task>
}