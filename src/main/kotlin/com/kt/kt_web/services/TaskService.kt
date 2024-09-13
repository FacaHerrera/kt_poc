package com.kt.kt_web.services

import com.kt.kt_web.entities.dto.StateDTO
import com.kt.kt_web.entities.dto.TaskDTO
import com.kt.kt_web.entities.model.State
import com.kt.kt_web.entities.model.StateOption
import com.kt.kt_web.entities.model.Task
import com.kt.kt_web.repositories.StateRepository
import com.kt.kt_web.repositories.TaskRepository
import jakarta.persistence.EntityNotFoundException
import org.hibernate.service.spi.ServiceException
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import shared.TicketDTO
import java.time.LocalDateTime
import java.util.*

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val stateRepository: StateRepository
) {
    @Transactional(readOnly=true)
    fun findAll(): List<TaskDTO> {
        return try {
            taskRepository.findAll().map { it.toDTO() }
        } catch (e: DataAccessException) {
            throw ServiceException("Error obtaining tasks", e)
        }
    }

    @Transactional(readOnly=true)
    fun findByTitle(title: String): Optional<TaskDTO> {
        return try {
            taskRepository.findByTitle(title).map { it.toDTO() }
        } catch (e: DataAccessException) {
            throw ServiceException("Error obtaining task by title", e)
        }
    }

    @Transactional(readOnly=true)
    fun findTaskStates(taskId: Long): List<StateDTO>? {
        return try {
            val states = stateRepository.findByTaskId(taskId)
            states.map { it.toDTO() }
        } catch (e: DataAccessException) {
            throw ServiceException("Error obtaining Task states")
        }
    }

    @Transactional(readOnly=false)
    fun save(taskDTO: TaskDTO): TaskDTO {
        return try {
            val task: Task = taskDTO.toEntity()
            val states: List<State>? = taskDTO.stateRecord?.map { it.toEntity() }

            val savedTask = taskRepository.save(task)
            val responseDTO = savedTask.toDTO()

            states?.forEach {
                it.task = savedTask
                val savedState = stateRepository.save(it)
                responseDTO.stateRecord?.add(savedState.toDTO())
            }

            responseDTO
        } catch (e: Exception) {
            throw ServiceException("Error saving task", e)
        }
    }

    @Transactional(readOnly=false)
    fun update(taskId: Long, taskDTO: TaskDTO): TaskDTO? {
        return try {
            val updatedTask = updateTask(taskId, taskDTO)

            val statesToUpdate = taskDTO.stateRecord?.map { stateDTO ->
                updateState(updatedTask, stateDTO)
            }

            if(statesToUpdate == null) return updatedTask.toDTO()

            val updatedStates = stateRepository.saveAll(statesToUpdate)

            updatedTask.toDTO(updatedStates)
        } catch (e: Exception) {
            throw ServiceException("Error updating task", e)
        }
    }

    private fun updateTask(taskId: Long, taskDTO: TaskDTO): Task {
        return try {
            val task = taskRepository.findById(taskId)
                .orElseThrow { EntityNotFoundException("Task not found") }

            task.title = taskDTO.title
            task.description = taskDTO.description

            taskRepository.save(task)
        } catch (e: Exception) {
            throw ServiceException("Error updating task")
        }
    }

    private fun updateState(task: Task, stateDTO: StateDTO): State {
        return try {
            val state = stateRepository.findById(stateDTO.id!!)
                .orElseGet { State() }

            state.state = StateOption.valueOf(stateDTO.state)
            state.dateFrom = stateDTO.dateFrom
            state.dateTo = stateDTO.dateTo
            state.task = task

            state
        } catch (e: Exception) {
            throw ServiceException("Error updating task state", e)
        }
    }

    @Transactional(readOnly=false)
    fun updateState(taskId: Long, stateDTO: StateDTO): StateDTO? {
        val task: Optional<Task> = taskRepository.findById(taskId)

        if(!task.isPresent) return null

        val taskToUpdate = task.get()
        val state = stateDTO.toEntity()
        state.task = taskToUpdate

        return stateRepository.save(state).toDTO()
    }

    @Transactional(readOnly=false)
    fun deleteEntity(taskDTO: TaskDTO) {
        try {
            taskRepository.delete(taskDTO.toEntity())
        } catch (e: Exception) {
            throw ServiceException("Error deleting task", e)
        }
    }

    @Transactional(readOnly=false)
    fun delete(id: Long) {
        try {
            taskRepository.deleteById(id)
        } catch (e: Exception) {
            throw ServiceException("Error deleting task", e)
        }
    }

    @Transactional(readOnly=false)
    fun processFromTicket(ticket: TicketDTO) {
        try {
            val states = mutableListOf(State(LocalDateTime.now(), null, StateOption.TO_DO, null))
            val task = Task(ticket.title, ticket.description)

            taskRepository.save(task)
            stateRepository.saveAll(states)
        } catch (e: Exception) {
            throw ServiceException("Error processing ticket", e)
        }
    }

    @Transactional(timeout = 5, propagation = Propagation.REQUIRES_NEW)
    fun saveWithTimeout(task: Task) = taskRepository.save(task)

    @Transactional(timeout = 5, propagation = Propagation.REQUIRES_NEW)
    fun findAllWithTimeout(): List<Task> = taskRepository.findAll()

}