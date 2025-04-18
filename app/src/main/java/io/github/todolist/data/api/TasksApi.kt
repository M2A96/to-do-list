package io.github.todolist.data.api

import io.github.todolist.data.dto.ToDoTaskDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TasksApi {
    @GET("tasks")
    suspend fun getAllTasks(): List<ToDoTaskDto>

    @POST("tasks")
    suspend fun createTask(@Body taskDto: ToDoTaskDto): ToDoTaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body taskDto: ToDoTaskDto): ToDoTaskDto

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Any
}