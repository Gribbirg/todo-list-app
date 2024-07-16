package ru.gribbirg.domain.model.user

import androidx.annotation.StringRes
import ru.gribbirg.todoapp.domain.R

enum class ThemeSettings(@StringRes val textId: Int) {
    Light(R.string.light_theme),
    Dark(R.string.dark_theme),
    LikeSystem(R.string.like_system_theme),
    ;
}