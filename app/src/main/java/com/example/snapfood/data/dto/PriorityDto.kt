package com.example.snapfood.data.dto

import androidx.compose.ui.graphics.Color
import com.example.snapfood.presentation.theme.HighPriorityColor
import com.example.snapfood.presentation.theme.LowPriorityColor
import com.example.snapfood.presentation.theme.MediumPriorityColor
import com.example.snapfood.presentation.theme.NonePriorityColor

enum class PriorityDto(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}