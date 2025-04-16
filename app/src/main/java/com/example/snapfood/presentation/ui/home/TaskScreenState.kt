package com.example.snapfood.presentation.ui.home

import com.example.snapfood.domain.model.ToDoTask

data class TaskScreenState (
    val searchQuery: String = "",
    val tasks: List<ToDoTask> = emptyList(),
    val isLoading: Boolean = false
)