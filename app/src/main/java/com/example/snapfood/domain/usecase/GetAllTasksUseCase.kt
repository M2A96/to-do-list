package com.example.snapfood.domain.usecase

import com.example.snapfood.domain.repository.AllTasksRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: AllTasksRepository
) {
    suspend operator fun invoke() = repository.getAllTasks()
}