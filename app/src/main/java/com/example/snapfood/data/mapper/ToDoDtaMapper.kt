package com.example.snapfood.data.mapper

import com.example.snapfood.data.dto.PriorityDto
import com.example.snapfood.data.dto.ToDoTaskDto
import com.example.snapfood.domain.model.Priority
import com.example.snapfood.domain.model.ToDoTask
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