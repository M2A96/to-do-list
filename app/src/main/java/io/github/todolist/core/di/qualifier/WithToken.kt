package io.github.todolist.core.di.qualifier

import javax.inject.Qualifier

/**
 * A qualifier to identify with-token api services
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WithToken
