package ru.gribbirg.todoapp.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toTimestamp() = atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

fun Long.toLocalDate(): LocalDate? = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()