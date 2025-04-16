package com.example.snapfood.domain.repository

import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.model.ToDoTask
import kotlinx.coroutines.flow.Flow

interface AllTasksRepository {
    suspend fun getAllTasks(): Flow<Resources<List<ToDoTask>>>
}