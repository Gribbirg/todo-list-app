package ru.gribbirg.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp


val Int.scaledSp
    @Composable get() = (this / LocalDensity.current.fontScale).sp