package com.example.snapfood.presentation.ui.home

import com.example.snapfood.domain.model.TaskStatus
import com.example.snapfood.domain.model.ToDoTask

sealed class TaskScreenEvent {
    data class OnSearchQueryChange(val query: String) : TaskScreenEvent()
    data class OnAddTaskClick(val toDoTask: ToDoTask) : TaskScreenEvent()
    data class OnTaskClick (val id : Int) : TaskScreenEvent()
    data class OnEditTaskClick (val id: Int) : TaskScreenEvent()
    data class OnDeleteTaskClick (val id: Int): TaskScreenEvent()
    data class OnTaskStatusChange(val id: Int, val status: TaskStatus) : TaskScreenEvent()
    data object LoadTasks : TaskScreenEvent()
}