package com.example.snapfood.domain.usecase

import com.example.snapfood.data.dto.ToDoTaskDto
import com.example.snapfood.domain.repository.AllTasksRepository
import com.example.snapfood.domain.repository.TaskRepository
import javax.inject.Inject

class EditTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task : ToDoTaskDto) = repository.editTask(task)
}