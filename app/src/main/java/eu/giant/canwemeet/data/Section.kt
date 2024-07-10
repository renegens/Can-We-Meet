package eu.giant.canwemeet.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

data class Section(var type: Type, var count: Int)

enum class Type(val icon: ImageVector) {
    Sleep(icon = Icons.Filled.Home),
    Coffee(icon = Icons.Filled.Call),
    Work(icon = Icons.Filled.AccountCircle),
    Home(icon = Icons.Filled.Place),
    Hidden(icon = Icons.Filled.Build)
}