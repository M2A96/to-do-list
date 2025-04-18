package com.example.snapfood.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.snapfood.presentation.theme.TodoListTheme

@Composable
fun StarWarsApp() {
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