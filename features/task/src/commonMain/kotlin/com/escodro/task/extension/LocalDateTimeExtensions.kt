package com.escodro.task.extension

import kotlinx.datetime.LocalDateTime

/**
 * Formats the [LocalDateTime] to a user-friendly string.
 */
expect fun LocalDateTime.format(): String
