package com.example.snapfood.core.di.module

import com.example.snapfood.data.repository.AllToDoTasksRepositoryImpl
import com.example.snapfood.data.repository.TaskRepositoryImpl
import com.example.snapfood.domain.repository.AllTasksRepository
import com.example.snapfood.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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