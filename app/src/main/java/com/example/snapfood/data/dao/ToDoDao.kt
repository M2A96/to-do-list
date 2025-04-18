package com.example.snapfood.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.snapfood.data.dto.ToDoTaskDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTasks(): Flow<List<ToDoTaskDto>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTaskDto>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: ToDoTaskDto)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: List<ToDoTaskDto>)

    @Update
    suspend fun updateTask(task: ToDoTaskDto) : Int

    @Delete
    suspend fun deleteTask(toDoTaskDto: ToDoTaskDto)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ToDoTaskDto>>

    @Query(
        """
        SELECT * FROM todo_table ORDER BY
    CASE
        WHEN priority LIKE 'L%' THEN 1
        WHEN priority LIKE 'M%' THEN 2
        WHEN priority LIKE 'H%' THEN 3
    END
        """
    )
    fun sortByLowPriority(): Flow<List<ToDoTaskDto>>

    @Query(
        """
        SELECT * FROM todo_table ORDER BY
    CASE
        WHEN priority LIKE 'H%' THEN 1
        WHEN priority LIKE 'M%' THEN 2
        WHEN priority LIKE 'L%' THEN 3
    END
        """
    )
    fun sortByHighPriority(): Flow<List<ToDoTaskDto>>
}