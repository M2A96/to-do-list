package com.example.snapfood.domain.repository

import com.example.snapfood.data.dto.ToDoTaskDto
import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.model.ToDoTask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: ToDoTask): Flow<Resources<Int>>
    suspend fun removeTask(task: ToDoTask): Flow<Resources<Int>>
    suspend fun editTask(task: ToDoTask): Flow<Resources<Int>>
    fun syncTasks(): Flow<Resources<Unit>>
}
