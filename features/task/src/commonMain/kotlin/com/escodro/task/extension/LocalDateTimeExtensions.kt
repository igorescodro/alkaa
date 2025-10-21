package com.escodro.task.extension

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

/**
 * Formats the [LocalDateTime] to a user-friendly string.
 */
@OptIn(ExperimentalTime::class)
expect fun LocalDateTime.format(): String
