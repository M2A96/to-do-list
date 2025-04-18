package io.github.todolist.data.api

import io.github.todolist.data.dto.PriorityDto
import io.github.todolist.data.dto.ToDoTaskDto
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Mock implementation of TaskApiService that simulates network behavior
 * with in-memory storage
 */
class MockTaskApiService @Inject constructor() : TasksApi {
    // In-memory storage for our "backend" data
    private val taskStorage = mutableListOf<ToDoTaskDto>()

    // Simulate random network delay
    private suspend fun simulateNetworkDelay() {
        delay(500L + (Math.random() * 1000).toLong())
    }

    // Simulate random network failures (20% chance)
    private fun shouldSimulateError(): Boolean {
        return Math.random() < 0.2
    }

    override suspend fun getAllTasks(): List<ToDoTaskDto> {
        simulateNetworkDelay()

        if (shouldSimulateError()) {
            throw Exception("Network error: Failed to fetch tasks")
        }

        return taskStorage.toList()
    }

    override suspend fun createTask(taskDto: ToDoTaskDto): ToDoTaskDto {
        simulateNetworkDelay()

        if (shouldSimulateError()) {
            throw Exception("Network error: Failed to create task")
        }

        // Create a copy with a server-generated ID
        val newTask = taskDto.copy(
            id = (1..100).random(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        taskStorage.add(newTask)
        return newTask
    }

    override suspend fun updateTask(id: Int, taskDto: ToDoTaskDto): ToDoTaskDto {
        simulateNetworkDelay()

        if (shouldSimulateError()) {
            throw Exception("Network error: Failed to update task")
        }

        val index = taskStorage.indexOfFirst { it.id == id }
        if (index == -1) {
            throw Exception("Task not found with id: $id")
        }

        val updatedTask = taskDto.copy(updatedAt = System.currentTimeMillis())
        taskStorage[index] = updatedTask
        return updatedTask
    }

    override suspend fun deleteTask(id: Int): Any {
        simulateNetworkDelay()

        if (shouldSimulateError()) {
            throw Exception("Network error: Failed to delete task")
        }

        val removed = taskStorage.removeIf { it.id == id }
        if (!removed) {
            throw Exception("Task not found with id: $id")
        }

        return Object() // Return empty object
    }

    // Helper function to pre-populate with sample data (optional)
    fun prepopulateWithSampleData() {
        val sampleTasks = listOf(
            ToDoTaskDto(
                id =0,
                title = "Buy groceries",
                description = "Milk, eggs, bread",
                priority = PriorityDto.MEDIUM,
                createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                updatedAt = System.currentTimeMillis() - 86400000
            ),
            ToDoTaskDto(
                id = 1,
                title = "Finish presentation",
                description = "For Monday's meeting",
                priority = PriorityDto.MEDIUM,
                createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
                updatedAt = System.currentTimeMillis() - 86400000
            ),
            ToDoTaskDto(
                id = 2,
                title = "Call dentist",
                description = "Schedule cleaning appointment",
                priority = PriorityDto.MEDIUM,
                createdAt = System.currentTimeMillis() - 259200000, // 3 days ago
                updatedAt = System.currentTimeMillis() - 172800000
            )
        )

        taskStorage.addAll(sampleTasks)
    }
}