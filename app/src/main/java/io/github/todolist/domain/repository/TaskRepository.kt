package io.github.todolist.domain.repository

import io.github.todolist.domain.model.Resources
import io.github.todolist.domain.model.ToDoTask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: ToDoTask): Flow<Resources<Int>>
    suspend fun removeTask(task: ToDoTask): Flow<Resources<Int>>
    suspend fun editTask(task: ToDoTask): Flow<Resources<Int>>
    fun syncTasks(): Flow<Resources<Unit>>
}
