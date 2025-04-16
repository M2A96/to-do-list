package com.example.snapfood.domain.usecase

import com.example.snapfood.data.dto.ToDoTaskDto
import com.example.snapfood.domain.repository.TaskRepository
import javax.inject.Inject

class RemoveTaskUseCase  @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId : Int) = repository.removeTask(taskId)
}