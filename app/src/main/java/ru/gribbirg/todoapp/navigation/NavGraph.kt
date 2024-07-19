package ru.gribbirg.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gribbirg.about.AboutScreen
import ru.gribbirg.edit.EditItemScreen
import ru.gribbirg.list.TodoListItemScreen
import ru.gribbirg.settings.SettingsScreen
import ru.gribbirg.theme.custom.AppTheme
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
            arguments = Screen.TodoList.arguments,
            enterTransition = TransitionAnimations.mainScreenEnter(animationDuration),
            exitTransition = TransitionAnimations.mainScreenExit(animationDuration),
        ) { backStackEntry ->
            val deleteId = backStackEntry.arguments?.getString(Screen.Edit.arguments.first().name)
            TodoListItemScreen(
                viewModel = listViewModel,
                deleteId = deleteId,
                toEditItemScreen = { id ->
                    navController.navigate(Screen.Edit.getRoute(itemId = id)) {
                        launchSingleTop = true
                    }
                },
                toSettingsScreen = {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                },
                toAboutScreen = {
                    navController.navigate(Screen.About.route) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(
            Screen.Edit.route,
            arguments = Screen.Edit.arguments,
            enterTransition = TransitionAnimations.secondScreenEnter(animationDuration),
            exitTransition = TransitionAnimations.secondScreenExit(animationDuration),
        ) { backStackEntry ->
            val viewModel = remember {
                editViewModelFactory
                    .create(backStackEntry.arguments?.getString(Screen.Edit.arguments.first().name))
            }
            EditItemScreen(
                viewModel = viewModel,
                onClose = { deleteId ->
                    listViewModel.deleteById(deleteId)
                    navController.popBackStack(Screen.TodoList.route, false)
                },
            )
        }
        composable(
            Screen.Settings.route,
            enterTransition = TransitionAnimations.secondScreenEnter(animationDuration),
            exitTransition = TransitionAnimations.secondScreenExit(animationDuration),
        ) {
            val viewModel = remember {
                appComponent.settingsFeatureComponent().settingsViewModel
            }
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(Screen.TodoList.route, false) }
            )
        }
        composable(
            Screen.About.route,
            enterTransition = TransitionAnimations.secondScreenEnter(animationDuration),
            exitTransition = TransitionAnimations.secondScreenExit(animationDuration),
        ) {
            val viewModel = remember {
                appComponent.aboutFeatureComponent().viewModel
            }
            AboutScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack(Screen.TodoList.route, false) }
            )
        }
    }
}