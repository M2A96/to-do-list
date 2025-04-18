package io.github.todolist.domain.usecase

import io.github.todolist.domain.repository.AllTasksRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: AllTasksRepository
) {
    suspend operator fun invoke() = repository.getAllTasks()
}