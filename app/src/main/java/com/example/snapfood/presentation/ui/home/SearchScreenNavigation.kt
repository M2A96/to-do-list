package com.example.snapfood.presentation.ui.home

sealed class SearchScreenNavigation {
    data class NavigateToDetails(val characterId: String) : SearchScreenNavigation()
}