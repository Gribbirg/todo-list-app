package ru.gribbirg.todoapp.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    data object TodoList : Screen("list")
    data object Edit : Screen("edit?id={id}", listOf(navArgument("id") { nullable = true })) {
        fun getRoute(itemId: String?) = itemId?.let { "edit?id=${itemId}" } ?: "edit"
    }
}