package com.example.snapfood.data.local.databse

import androidx.room.RoomDatabase
import com.example.snapfood.data.dao.ToDoDao

abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDAO(): ToDoDao
}