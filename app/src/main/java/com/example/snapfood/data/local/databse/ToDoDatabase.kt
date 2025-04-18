package com.example.snapfood.data.local.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.snapfood.data.dao.ToDoDao
import com.example.snapfood.data.dto.ToDoTaskDto

@Database(entities = [ToDoTaskDto::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDAO(): ToDoDao
}