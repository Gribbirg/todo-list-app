package ru.gribbirg.utils.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Extensions for conversions between LocalData(Time) and Long
 */
fun LocalDate.toTimestamp(zoneId: ZoneId = ZoneId.systemDefault()) =
    atStartOfDay(zoneId)?.toInstant()?.toEpochMilli()

fun Long.toLocalDate(): LocalDate? =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDateTime.toTimestamp(zoneId: ZoneId = ZoneId.of("UTC")) =
    atZone(zoneId).toInstant().toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime? =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

val currentLocalDateTimeAtUtc
    get() = LocalDateTime.now(ZoneId.of("UTC"))