package ru.gribbirg.todoapp.utils

import kotlin.math.pow
import kotlin.math.roundToInt

internal fun Double.roundTo(count: Int) =
    (this * 10.0.pow(count)).roundToInt() / 10.0.pow(count)