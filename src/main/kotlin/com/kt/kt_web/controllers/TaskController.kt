package com.kt.kt_web.controllers

import com.kt.kt_web.entities.dto.StateDTO
import com.kt.kt_web.entities.dto.TaskDTO
import com.kt.kt_web.services.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(private val service: TaskService) {

    @GetMapping("")
    fun findAll() = service.findAll()

    @GetMapping("/title")
    fun findByTitle(@RequestParam("title") title: String): ResponseEntity<TaskDTO> {
        return service.findByTitle(title)
            .map { task -> ResponseEntity.ok(task) }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/{taskId}/states")
    fun findTaskStates(@PathVariable("taskId") taskId: Long) = service.findTaskStates(taskId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping("")
    fun save(@RequestBody taskDTO: TaskDTO) = service.save(taskDTO)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody taskDTO: TaskDTO) = service.update(id, taskDTO) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PatchMapping("/{id}/state")
    fun updateState(@PathVariable id: Long, @RequestBody stateDTO: StateDTO) = service.updateState(id, stateDTO) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) = service.delete(id)

    @DeleteMapping("")
    fun deleteEntity(@RequestBody taskDTO: TaskDTO) = service.deleteEntity(taskDTO)
}