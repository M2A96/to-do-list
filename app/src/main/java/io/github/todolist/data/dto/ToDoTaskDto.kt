package io.github.todolist.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class ToDoTaskDto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: PriorityDto,
    val createdAt: Long,
    val updatedAt: Long,
)
