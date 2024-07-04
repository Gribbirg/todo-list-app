package ru.gribbirg.todoapp.data.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.gribbirg.todoapp.R

enum class TodoImportance(
    val power: Int,
    @StringRes val nameId: Int,
    @DrawableRes val logoId: Int? = null,
    @ColorRes val colorId: Int? = null,
) {
    NO(
        power = 0,
        nameId = R.string.importence_no
    ),
    LOW(
        power = 1,
        nameId = R.string.importance_low,
        logoId = R.drawable.baseline_south_24
    ),
    HIGH(
        power = 2,
        nameId = R.string.importance_high,
        logoId = R.drawable.round_priority_high_24,
        colorId = R.color.red
    );

    companion object
}