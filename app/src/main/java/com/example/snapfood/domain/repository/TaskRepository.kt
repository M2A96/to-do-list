package com.example.snapfood.domain.repository

import com.example.snapfood.data.dto.ToDoTaskDto
import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.model.ToDoTask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: ToDoTaskDto): Flow<Resources<Int>>
    suspend fun removeTask(taskId: Int): Flow<Resources<Int>>
    suspend fun editTask(task: ToDoTaskDto): Flow<Resources<Int>>
}
