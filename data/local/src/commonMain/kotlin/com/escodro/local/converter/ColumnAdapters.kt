package com.escodro.local.converter

import app.cash.sqldelight.ColumnAdapter
import com.escodro.local.model.AlarmInterval
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Converts between [LocalDateTime] and [Long] to be stored in the database.
 */
@OptIn(ExperimentalTime::class)
val dateTimeAdapter = object : ColumnAdapter<LocalDateTime, Long> {
    override fun decode(databaseValue: Long): LocalDateTime =
        Instant
            .fromEpochMilliseconds(databaseValue)
            .toLocalDateTime(TimeZone.currentSystemDefault())

    override fun encode(value: LocalDateTime): Long =
        value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

/**
 * Converts between [AlarmInterval] and [Long] to be stored in the database.
 */
val alarmIntervalAdapter = object : ColumnAdapter<AlarmInterval, Long> {
    override fun decode(databaseValue: Long): AlarmInterval =
        AlarmInterval.entries.find { it.id == databaseValue.toInt() }!!

    override fun encode(value: AlarmInterval): Long =
        value.id.toLong()
}
