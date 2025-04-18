package com.example.snapfood.domain.usecase

import com.example.snapfood.domain.model.ToDoTask
import com.example.snapfood.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: ToDoTask) = repository.addTask(task)
}