package com.escodro.domain.provider

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class DateTimeProviderImpl : DateTimeProvider {

    override fun getCurrentInstant(): Instant = Clock.System.now()

    override fun getCurrentLocalDateTime(): LocalDateTime = getCurrentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
