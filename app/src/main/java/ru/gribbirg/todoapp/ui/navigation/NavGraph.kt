package ru.gribbirg.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gribbirg.todoapp.ui.edititem.EditItemScreen
import ru.gribbirg.todoapp.ui.edititem.EditItemViewModel
import ru.gribbirg.todoapp.ui.todoitemslist.TodoItemsListViewModel
import ru.gribbirg.todoapp.ui.todoitemslist.TodoListItemScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    listViewModel: TodoItemsListViewModel,
    editItemViewModel: EditItemViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            TodoListItemScreen(
                viewModel = listViewModel,
                toEditItemScreen = { item ->
                    editItemViewModel.setItem(item)
                    navController.navigate(Screen.Edit.route)
                }
            )
        }
        composable(Screen.Edit.route) {
            EditItemScreen(viewModel = editItemViewModel)
        }
    }
}