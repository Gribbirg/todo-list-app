package ru.gribbirg.todoapp.ui.navigation

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Edit : Screen("edit")
}