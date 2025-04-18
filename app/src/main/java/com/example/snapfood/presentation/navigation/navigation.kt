package com.example.snapfood.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.snapfood.presentation.ui.home.HomeViewModel
import com.example.todolist.presentation.ui.home.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
}

fun NavGraphBuilder.starWarsNavGraph(navController: NavHostController) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        HomeScreen (
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}