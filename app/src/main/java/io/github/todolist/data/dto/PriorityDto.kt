package io.github.todolist.data.dto

import androidx.compose.ui.graphics.Color
import io.github.todolist.presentation.theme.HighPriorityColor
import io.github.todolist.presentation.theme.LowPriorityColor
import io.github.todolist.presentation.theme.MediumPriorityColor
import io.github.todolist.presentation.theme.NonePriorityColor

enum class PriorityDto(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}