package ru.gribbirg.todoapp.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        startDestination = Screen.List.route,
    ) {
        composable(
            Screen.List.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 300),
                    initialAlpha = 0.999f
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 300),
                    targetAlpha = 0.999f
                )
            },
        ) {
            TodoListItemScreen(
                viewModel = listViewModel,
                toEditItemScreen = { item ->
                    editItemViewModel.setItem(item)
                    navController.navigate(Screen.Edit.route)
                }
            )
        }
        composable(
            Screen.Edit.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
        ) {
            EditItemScreen(
                viewModel = editItemViewModel,
                onClose = { navController.popBackStack() })
        }
    }
}