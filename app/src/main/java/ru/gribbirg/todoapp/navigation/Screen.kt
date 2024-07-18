package ru.gribbirg.todoapp.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

/**
 * Routes for navigation
 */
internal sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object TodoList :
        Screen("list?deleteId={deleteId}", listOf(navArgument("deleteId") { nullable = true })) {

        fun getRoute(deleteId: String?) = deleteId?.let { "delete?deleteId=${deleteId}" } ?: "list"
    }

    data object Edit : Screen("edit?id={id}", listOf(navArgument("id") { nullable = true })) {

        fun getRoute(itemId: String?) = itemId?.let { "edit?id=${itemId}" } ?: "edit"
    }

    data object Settings : Screen("settings")

    data object About : Screen("about")
}