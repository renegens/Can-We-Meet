package eu.giant.canwemeet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import eu.giant.canwemeet.presentation.ui.main_screen.MainScreen
import eu.giant.canwemeet.presentation.ui.theme.CanWeMeetTheme

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            CanWeMeetTheme {
                MainScreen()
            }
        }
    }
}