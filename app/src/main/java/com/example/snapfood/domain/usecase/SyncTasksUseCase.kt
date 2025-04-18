package com.example.snapfood.domain.usecase

import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<Resources<Unit>> {
        return taskRepository.syncTasks()
    }
}