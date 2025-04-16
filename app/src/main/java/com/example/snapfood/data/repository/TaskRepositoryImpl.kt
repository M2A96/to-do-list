package com.example.snapfood.data.repository

import com.example.snapfood.data.dao.ToDoDao
import com.example.snapfood.data.dto.ToDoTaskDto
import com.example.snapfood.data.mapper.ToDoDtaMapper
import com.example.snapfood.domain.model.Resources
import com.example.snapfood.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: ToDoDao,
    private val mapper: ToDoDtaMapper
): TaskRepository {
    override suspend fun addTask(task: ToDoTaskDto): Flow<Resources<Int>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                val taskId = dao.addTask(task)
                emit(Resources.Success(taskId))
                emit(Resources.Loading(false))

            }  catch (e: Exception) {
                emit(Resources.Error("Could not add Task"))
                emit(Resources.Loading(false))
            }
        }
    }

    override suspend fun removeTask(taskId: Int): Flow<Resources<Int>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                val deletedTaskId = dao.deleteTask(taskId)
                emit(Resources.Success(deletedTaskId))
                emit(Resources.Loading(false))

            }  catch (e: Exception) {
                emit(Resources.Error("Could not remove Task"))
                emit(Resources.Loading(false))
            }
        }
    }

    override suspend fun editTask(task: ToDoTaskDto): Flow<Resources<Int>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                val rowsAffected = dao.updateTask(task)
                emit(Resources.Success(rowsAffected))
                emit(Resources.Loading(false))

            }  catch (e: Exception) {
                emit(Resources.Error("Could not edit Task"))
                emit(Resources.Loading(false))
            }
        }
    }

}