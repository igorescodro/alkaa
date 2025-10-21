package com.escodro.domain.usecase.fake

import com.escodro.domain.provider.DateTimeProvider
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class DateTimeProviderFake : DateTimeProvider {
    override fun getCurrentInstant(): Instant =
        Instant.fromEpochMilliseconds(734_892_600_000) // 1993-04-15T16:50:00Z

    override fun getCurrentLocalDateTime(): LocalDateTime = getCurrentInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
