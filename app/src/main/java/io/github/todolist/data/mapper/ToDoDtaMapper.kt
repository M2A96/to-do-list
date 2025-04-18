package io.github.todolist.data.mapper

import io.github.todolist.data.dto.PriorityDto
import io.github.todolist.data.dto.ToDoTaskDto
import io.github.todolist.domain.model.Priority
import io.github.todolist.domain.model.ToDoTask
import jakarta.inject.Inject

class ToDoDtaMapper  @Inject constructor()  {
    fun toDomain(dto: ToDoTaskDto) = ToDoTask(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        priority = when (dto.priority) {
            PriorityDto.HIGH -> Priority.HIGH
            PriorityDto.MEDIUM -> Priority.MEDIUM
            PriorityDto.LOW -> Priority.LOW
            PriorityDto.NONE -> Priority.NONE
        }
    )

    fun toDto(domain: ToDoTask) = ToDoTaskDto(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        priority = when (domain.priority) {
            Priority.HIGH -> PriorityDto.HIGH
            Priority.LOW -> PriorityDto.LOW
            Priority.MEDIUM -> PriorityDto.MEDIUM
            Priority.NONE -> PriorityDto.NONE
        },
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
    )
}