package io.github.todolist.domain.repository

import io.github.todolist.domain.model.Resources
import io.github.todolist.domain.model.ToDoTask
import kotlinx.coroutines.flow.Flow

interface AllTasksRepository {
    suspend fun getAllTasks(): Flow<Resources<List<ToDoTask>>>
}