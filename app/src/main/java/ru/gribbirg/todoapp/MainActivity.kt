package ru.gribbirg.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import ru.gribbirg.theme.custom.TodoAppTheme
import ru.gribbirg.todoapp.navigation.NavGraph

/**
 * Main and single app activity
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoComposeApp()
        }
    }

    @Composable
    fun TodoComposeApp() {
        val navController = rememberNavController()
        val appComponent = (applicationContext as TodoApplication).appComponent
        val settings by appComponent.settingsHandler().getSettings().collectAsState()

        TodoAppTheme(
            theme = settings.theme,
        ) {
            NavGraph(
                navController = navController,
                appComponent = appComponent,
            )
        }
    }
}