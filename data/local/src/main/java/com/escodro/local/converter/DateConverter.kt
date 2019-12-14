package com.escodro.local.converter

import androidx.room.TypeConverter
import java.util.Calendar

/**
 * Converts the [Calendar] to [Long] and vice versa to be stored in the database.
 */
internal class DateConverter {

    /**
     * Converts a timestamp in [Long] format to [Calendar].
     *
     * @param timestamp timestamp to be converted
     *
     * @return [Calendar] from timestamp
     */
    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Calendar? {
        if (timestamp == null) {
            return null
        }

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar
    }

    /**
     * Converts a [Calendar] to timestamp in [Long] format.
     *
     * @param calendar to be converted
     *
     * @return timestamp in [Long] format
     */
    @TypeConverter
    fun calendarToTimestamp(calendar: Calendar?): Long? {
        if (calendar == null) {
            return null
        }

        return calendar.timeInMillis
    }
}
