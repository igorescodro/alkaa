package com.escodro.task.presentation.compose.saver

import androidx.compose.runtime.saveable.Saver
import kotlinx.datetime.LocalDateTime

/**
 * Saver to save and restore [LocalDateTime] in a [String].
 */
val LocalDateTimeSaver: Saver<LocalDateTime?, String> = Saver(
    save = { value -> value.toString() },
    restore = { value -> LocalDateTime.parse(value) },
)
