package com.example.snapfood.presentation.ui.home

sealed class SearchScreenEvent {
    data class OnSearchQueryChange(val query: String) : SearchScreenEvent()
    data object LoadInitialCharacters : SearchScreenEvent()
}