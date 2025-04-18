package io.github.todolist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.todolist.presentation.theme.TodoListTheme

@Composable
fun ToDoListApp() {
    TodoListTheme {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            starWarsNavGraph(navController)
        }
    }
}