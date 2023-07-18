package com.escodro.domain.usecase.fake

import com.escodro.domain.provider.CalendarProvider
import java.util.Calendar
import java.util.GregorianCalendar

internal class CalendarProviderFake : CalendarProvider {

    override fun getCurrentCalendar(): Calendar =
        GregorianCalendar(1993, 3, 15, 16, 50, 0)
}
