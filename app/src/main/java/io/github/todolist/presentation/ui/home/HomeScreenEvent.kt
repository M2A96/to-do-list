package io.github.todolist.presentation.ui.home

import io.github.todolist.domain.model.TaskStatus
import io.github.todolist.domain.model.ToDoTask


sealed class HomeScreenEvent {
    data class OnSearchQueryChange(val query: String) : HomeScreenEvent()
    data class OnAddTaskClick(val toDoTask: ToDoTask) : HomeScreenEvent()
    data class OnTaskClick (val task : ToDoTask) : HomeScreenEvent()
    data class OnEditTaskClick (val editedTask: ToDoTask) : HomeScreenEvent()
    data class OnDeleteTaskClick (val task: ToDoTask): HomeScreenEvent()
    data class OnTaskStatusChange(val id: Int, val status: TaskStatus) : HomeScreenEvent()
    data object LoadTasks : HomeScreenEvent()
    data class OnStatusFilterChange(val status: TaskStatus?) : HomeScreenEvent()
}