package com.example.snapfood.domain.model

data class ToDoTask (
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
)