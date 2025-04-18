package io.github.todolist.data.local.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.todolist.data.dao.ToDoDao
import io.github.todolist.data.dto.ToDoTaskDto

@Database(entities = [ToDoTaskDto::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDAO(): ToDoDao
}