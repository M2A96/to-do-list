package io.github.todolist.core.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.todolist.data.repository.AllToDoTasksRepositoryImpl
import io.github.todolist.data.repository.TaskRepositoryImpl
import io.github.todolist.domain.repository.AllTasksRepository
import io.github.todolist.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAllTasksRepository(
        allTasksRepositoryImpl: AllToDoTasksRepositoryImpl
    ): AllTasksRepository

    @Binds
    @Singleton
    abstract fun bindTasksRepository(
        tasksRepository: TaskRepositoryImpl
    ): TaskRepository
}