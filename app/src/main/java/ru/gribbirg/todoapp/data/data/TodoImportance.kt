package ru.gribbirg.todoapp.data.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.gribbirg.todoapp.R

/**
 * Importance of item
 *
 * @see TodoItem
 */
enum class TodoImportance(
    val power: Int,
    @StringRes val nameId: Int,
    @DrawableRes val logoId: Int? = null,
    @ColorRes val colorId: Int? = null,
) {
    No(
        power = 0,
        nameId = R.string.importence_no
    ),
    Low(
        power = 1,
        nameId = R.string.importance_low,
        logoId = R.drawable.baseline_south_24
    ),
    High(
        power = 2,
        nameId = R.string.importance_high,
        logoId = R.drawable.round_priority_high_24,
        colorId = R.color.red
    );

    companion object
}