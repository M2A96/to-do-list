package io.github.todolist.data.repository

import android.util.Log
import io.github.todolist.data.api.TasksApi
import io.github.todolist.data.dao.ToDoDao
import io.github.todolist.data.mapper.ToDoDtaMapper
import io.github.todolist.domain.model.Resources
import io.github.todolist.domain.model.ToDoTask
import io.github.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: ToDoDao,
    private val mapper: ToDoDtaMapper,
    private val tasksApi: TasksApi

): TaskRepository {
    override suspend fun addTask(task: ToDoTask): Flow<Resources<Int>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                // Save to local DB first
                dao.addTask(mapper.toDto(task))

                // Try to sync with server (but don't block on failure)
                try {
                    val createdTask = tasksApi.createTask(mapper.toDto(task))
                    // Update the local task with the server-generated ID if different
                    if (createdTask.id != task.id) {
                        dao.deleteTask(mapper.toDto(task))
                        dao.addTask(mapper.toDto(mapper.toDomain(createdTask)))
                    }
                } catch (e: Exception) {
                    // Just log the error, don't fail the operation
                    Log.w("", "Failed to sync new task with server", e)
                }

                emit(Resources.Success(1))
            } catch (e: Exception) {
                Log.e("TaskRepositoryImpl", "Error adding task", e)
                emit(Resources.Error(e.localizedMessage ?: "Failed to add task"))
            } finally {
                emit(Resources.Loading(false))
            }
        }
    }

    override suspend fun removeTask(task: ToDoTask): Flow<Resources<Int>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                // Delete from local DB
                dao.deleteTask(mapper.toDto(task))

                // Try to sync with server
                try {
                    tasksApi.deleteTask(task.id)
                } catch (e: Exception) {
                    // Just log the error
                    Log.w("TaskRepositoryImpl", "Failed to sync deleted task with server", e)
                }

                emit(Resources.Success(1))
            } catch (e: Exception) {
                Log.e("TaskRepositoryImpl", "Error deleting task", e)
                emit(Resources.Error(e.localizedMessage ?: "Failed to delete task"))
            } finally {
                emit(Resources.Loading(false))
            }
        }
    }

    override suspend fun editTask(task: ToDoTask): Flow<Resources<Int>> {
        return flow {
            emit(Resources.Loading(true))
            try {
                // Update local DB
                dao.updateTask(mapper.toDto(task))

                // Try to sync with server
                try {
                    tasksApi.updateTask(task.id, mapper.toDto(task))
                } catch (e: Exception) {
                    // Just log the error
                    Log.w("TaskRepositoryImpl", "Failed to sync updated task with server", e)
                }

                emit(Resources.Success(1))
            } catch (e: Exception) {
                Log.e("TaskRepositoryImpl", "Error updating task", e)
                emit(Resources.Error(e.localizedMessage ?: "Failed to update task"))
            } finally {
                emit(Resources.Loading(false))
            }
        }
    }

    override fun syncTasks(): Flow<Resources<Unit>> = flow {
        emit(Resources.Loading(true))
        try {
            // Fetch all tasks from API
            val remoteTasks = tasksApi.getAllTasks()
            Log.d("TaskRepositoryImpl", "Synced ${remoteTasks.size} tasks from server")

            // Convert to entities and save to DB
            val taskEntities = remoteTasks.map {
                mapper.toDto(mapper.toDomain(it))
            }

            dao.addTask(taskEntities)

            emit(Resources.Success(Unit))
        } catch (e: Exception) {
            Log.e("TaskRepositoryImpl", "Error syncing tasks with server", e)
            emit(Resources.Error(e.localizedMessage ?: "Failed to sync tasks"))
        } finally {
            emit(Resources.Loading(false))
        }
    }
}