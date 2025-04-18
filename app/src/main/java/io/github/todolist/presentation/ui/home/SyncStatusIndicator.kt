package io.github.todolist.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SyncStatusIndicator(
    syncStatus: SyncStatus,
    errorMessage: String?,
    onDismiss: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Auto-hide success message after delay
    LaunchedEffect(syncStatus) {
        if (syncStatus == SyncStatus.SUCCESS) {
            showSuccessMessage = true
            coroutineScope.launch {
                delay(2000)
                showSuccessMessage = false
                onDismiss()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (syncStatus) {
            SyncStatus.SYNCING -> {
                SyncMessage(
                    icon = Icons.Default.Refresh,
                    message = "Syncing with server...",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            SyncStatus.SUCCESS -> {
                AnimatedVisibility(
                    visible = showSuccessMessage,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SyncMessage(
                        icon = Icons.Default.Check,
                        message = "Sync completed successfully",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            SyncStatus.ERROR -> {
                SyncMessage(
                    icon = Icons.Default.Warning,
                    message = errorMessage ?: "Sync failed",
                    color = MaterialTheme.colorScheme.error
                )
            }
            SyncStatus.NONE -> {
                // Show nothing
            }
        }
    }
}

@Composable
private fun SyncMessage(
    icon: ImageVector,
    message: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}