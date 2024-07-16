package ru.gribbirg.todoapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gribbirg.edit.EditItemScreen
import ru.gribbirg.list.TodoListItemScreen
import ru.gribbirg.settings.SettingsScreen
import ru.gribbirg.theme.AppTheme
import ru.gribbirg.todoapp.di.AppComponent

/**
 * Navigation graph
 */
@Composable
internal fun NavGraph(
    navController: NavHostController,
    appComponent: AppComponent,
) {
    val animationDuration = AppTheme.dimensions.animationDurationNavigationTransition

    val listViewModel = remember {
        appComponent.listFeatureComponent().listViewModel()
    }

    val editViewModelFactory = remember {
        appComponent.editFeatureComponent().editFeatureFactory().createViewModelFactory()
    }

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
                },
                toSettingsScreen = {
                    navController.navigate(Screen.Settings.route) {
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
        ) { backStackEntry ->
            val viewModel = remember {
                editViewModelFactory
                    .create(backStackEntry.arguments?.getString(Screen.Edit.arguments.first().name))
            }
            EditItemScreen(
                viewModel = viewModel,
                onClose = {
                    navController.popBackStack(Screen.TodoList.route, false)
                },
            )
        }
        composable(
            Screen.Settings.route,
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
            val viewModel = remember {
                appComponent.settingsFeatureComponent().settingsViewModel
            }
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(Screen.TodoList.route, false) }
            )
        }
    }
}