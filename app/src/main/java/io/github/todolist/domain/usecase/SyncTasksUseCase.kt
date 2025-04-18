package io.github.todolist.domain.usecase

import io.github.todolist.domain.model.Resources
import io.github.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<Resources<Unit>> {
        return taskRepository.syncTasks()
    }
}