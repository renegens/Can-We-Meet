package eu.giant.canwemeet.presentation.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TimeSection() {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.5f)) {
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Gray)
            )
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Blue)
            )
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Magenta)
            )
        }

        Column(modifier = Modifier.weight(0.5f)) {
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Gray)
            )
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Blue)
            )
            Box(
                modifier = Modifier
                    .weight(0.2F)
                    .background(Color.Magenta)
            )
        }
    }
}