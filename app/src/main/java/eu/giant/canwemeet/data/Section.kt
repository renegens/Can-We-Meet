package eu.giant.canwemeet.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

data class Section(var type: Type, var count: Int)

enum class Type(val defaultAnchor: Int, val cutOff: Int, val icon: ImageVector) {
    Sleep(defaultAnchor = 3, cutOff = 6,icon = Icons.Filled.Home),
    Coffee(defaultAnchor = 8,cutOff = 10, icon = Icons.Filled.Call),
    Work(defaultAnchor = 13, cutOff = 16, icon = Icons.Filled.AccountCircle),
    Home(defaultAnchor = 20, cutOff = 23, icon = Icons.Filled.Place),
    Hidden(defaultAnchor = -1, cutOff = 2, icon = Icons.Filled.Build)
}
