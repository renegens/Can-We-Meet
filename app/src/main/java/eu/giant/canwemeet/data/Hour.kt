package eu.giant.canwemeet.data

import androidx.compose.ui.graphics.Color

data class Hour(val hour: Int, val type: Type, val color: Color)

enum class Item {
    Box, Row
}