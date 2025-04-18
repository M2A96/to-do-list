package io.github.todolist.domain.model

import java.time.LocalDate

data class ToDoTask (
    val id: Int = 0,
    val title: String= "",
    val description: String = "",
    val priority: Priority = Priority.HIGH,
    val status : TaskStatus = TaskStatus.UNDONE,
    val addedDate: LocalDate = LocalDate.now()
) {
}