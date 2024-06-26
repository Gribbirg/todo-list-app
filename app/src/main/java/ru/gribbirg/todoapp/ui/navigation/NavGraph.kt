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
        startDestination = Screen.TodoList.route,
    ) {
        composable(
            Screen.TodoList.route,
            arguments = Screen.Edit.arguments,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 500),
                    initialAlpha = 0.999f
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 500),
                    targetAlpha = 0.999f
                )
            },
        ) {
            TodoListItemScreen(
                viewModel = listViewModel,
                toEditItemScreen = { id ->
                    navController.navigate(Screen.Edit.getRoute(itemId = id)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            Screen.Edit.route,
            arguments = Screen.Edit.arguments,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        500,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(
                        500,
                        easing = FastOutSlowInEasing
                    )
                )
            },
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString(Screen.Edit.arguments.first().name)
            EditItemScreen(
                itemId = itemId,
                viewModel = editItemViewModel,
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}