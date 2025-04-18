package io.github.todolist.domain.usecase

import io.github.todolist.domain.model.ToDoTask
import io.github.todolist.domain.repository.TaskRepository
import javax.inject.Inject

class RemoveTaskUseCase  @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task : ToDoTask) = repository.removeTask(task)
}