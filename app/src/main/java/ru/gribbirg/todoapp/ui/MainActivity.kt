package ru.gribbirg.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.gribbirg.todoapp.ui.edititem.EditItemViewModel
import ru.gribbirg.todoapp.ui.navigation.NavGraph
import ru.gribbirg.todoapp.ui.theme.TodoAppTheme
import ru.gribbirg.todoapp.ui.todoitemslist.TodoItemsListViewModel

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
        val listViewModel: TodoItemsListViewModel by viewModels { TodoItemsListViewModel.Factory }
        val editItemViewModel: EditItemViewModel by viewModels { EditItemViewModel.Factory }

        NavGraph(
            navController = navController,
            listViewModel = listViewModel,
            editItemViewModel = editItemViewModel
        )
    }
}