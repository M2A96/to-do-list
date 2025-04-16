package com.example.snapfood.data.repository

import com.example.snapfood.data.dao.ToDoDao
import com.example.snapfood.data.mapper.ToDoDtaMapper
import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.model.ToDoTask
import com.example.snapfood.domain.repository.AllTasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AllToDoTasksRepositoryImpl @Inject constructor(
    private val dao: ToDoDao,
    private val mapper: ToDoDtaMapper
) : AllTasksRepository {
    override suspend fun getAllTasks(): Flow<Resources<List<ToDoTask>>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                val response = dao.getAllTasks()
                val toDoTasks = response
                    .map {
                    mapper.toDomain(it)
                    }
                emit(Resources.Success(toDoTasks))
                emit(Resources.Loading(false))
            } catch (e: Exception) {
                emit(Resources.Error("Could not load characters"))
                emit(Resources.Loading(false))
            }
        }
    }
}