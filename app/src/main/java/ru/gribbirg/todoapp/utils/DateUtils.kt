package ru.gribbirg.todoapp.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDate.toTimestamp(zoneId: ZoneId = ZoneId.systemDefault()) =
    atStartOfDay(zoneId)?.toInstant()?.toEpochMilli()

fun Long.toLocalDate(): LocalDate? =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDateTime.toTimestamp(zoneId: ZoneId = ZoneId.of("UTC")) =
    atZone(zoneId).toInstant().toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime? =
    Instant.ofEpochMilli(this).atZone(ZoneId.of("UTC")).toLocalDateTime()