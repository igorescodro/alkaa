package com.escodro.task.presentation.fake

import com.escodro.task.provider.RelativeDateTimeProvider
import kotlinx.datetime.LocalDateTime

internal class RelativeDateTimeProviderFake : RelativeDateTimeProvider {
    override fun toRelativeDateTimeString(dateTime: LocalDateTime): String =
        "${dateTime.hour}:${dateTime.minute}"
}
