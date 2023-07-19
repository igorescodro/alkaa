package com.escodro.domain.provider

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class DateTimeProviderImpl : DateTimeProvider {

    override fun getCurrentInstant(): Instant = Clock.System.now()

    override fun getCurrentLocalDateTime(): LocalDateTime = getCurrentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
