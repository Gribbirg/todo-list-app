package ru.gribbirg.todoapp.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gribbirg.todoapp.ui.screens.edititem.EditItemScreen
import ru.gribbirg.todoapp.ui.screens.todoitemslist.TodoItemsListViewModel
import ru.gribbirg.todoapp.ui.screens.todoitemslist.TodoListItemScreen
import ru.gribbirg.todoapp.ui.theme.AppTheme

/**
 * Navigation graph
 */
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    val listViewModel: TodoItemsListViewModel = viewModel(factory = TodoItemsListViewModel.Factory)
    val animationDuration = AppTheme.dimensions.animationDurationNavigationTransition
    NavHost(
        navController = navController,
        startDestination = Screen.TodoList.route,
    ) {
        composable(
            Screen.TodoList.route,
            arguments = Screen.Edit.arguments,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = animationDuration),
                    initialAlpha = 0.999f
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = animationDuration),
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
                        animationDuration,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(
                        animationDuration,
                        easing = FastOutSlowInEasing
                    )
                )
            },
        ) {
            EditItemScreen(
                onClose = {
                    navController.popBackStack(Screen.TodoList.route, false)
                }
            )
        }
    }
}