package io.github.todolist.presentation.ui.home

import io.github.todolist.domain.model.TaskStatus
import io.github.todolist.domain.model.ToDoTask


data class TaskScreenState (
    val searchQuery: String = "",
    val currentTask: ToDoTask = ToDoTask(),
    val tasks: List<ToDoTask> = emptyList(),
    val isLoading: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCING,
    val statusFilter: TaskStatus? = null,
    val errorMessage: String = ""
)