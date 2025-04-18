package com.example.snapfood.data.repository

import android.util.Log
import com.example.snapfood.data.dao.ToDoDao
import com.example.snapfood.data.mapper.ToDoDtaMapper
import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.model.ToDoTask
import com.example.snapfood.domain.repository.AllTasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AllToDoTasksRepositoryImpl @Inject constructor(
    private val dao: ToDoDao,
    private val mapper: ToDoDtaMapper
) : AllTasksRepository {
    override suspend fun getAllTasks(): Flow<Resources<List<ToDoTask>>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                // Always read from local DB first (offline-first)
               dao.getAllTasks().collect { taskDtos ->
                   val tasks = taskDtos.map { dto -> mapper.toDomain(dto) }
                   emit(Resources.Success(tasks))
               }
            } catch (e: Exception) {
                Log.e("AllToDoTasksRepositoryImpl", "Error getting tasks from database", e)
                emit(Resources.Error(e.localizedMessage ?: "Failed to load tasks"))
            } finally {
                emit(Resources.Loading(false))
            }
        }
    }
}