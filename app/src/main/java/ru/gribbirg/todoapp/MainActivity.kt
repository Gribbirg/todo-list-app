package ru.gribbirg.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.gribbirg.todoapp.navigation.NavGraph
import ru.gribbirg.ui.theme.TodoAppTheme

/**
 * Main and single app activity
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                TodoComposeApp()
            }
        }
    }

    @Composable
    fun TodoComposeApp() {
        val navController = rememberNavController()
        val appComponent = (applicationContext as TodoApplication).appComponent

        NavGraph(
            navController = navController,
            appComponent = appComponent,
        )
    }
}