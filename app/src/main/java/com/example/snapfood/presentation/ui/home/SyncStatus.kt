package com.example.snapfood.presentation.ui.home

enum class SyncStatus {
    NONE,       // Initial state or when no sync is happening
    SYNCING,    // Currently syncing with server
    SUCCESS,    // Sync completed successfully
    ERROR       // Sync failed
}