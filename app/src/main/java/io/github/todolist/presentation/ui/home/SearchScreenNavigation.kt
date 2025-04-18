package io.github.todolist.presentation.ui.home

sealed class SearchScreenNavigation {
    data class NavigateToDetails(val characterId: String) : SearchScreenNavigation()
}