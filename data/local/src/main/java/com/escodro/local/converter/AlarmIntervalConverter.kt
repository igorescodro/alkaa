package com.escodro.local.converter

import androidx.room.TypeConverter
import com.escodro.local.model.AlarmInterval

/**
 * Converts between Alarm Interval and Int.
 */
internal class AlarmIntervalConverter {

    /**
     * Converts a id in Alarm Interval.
     *
     * @param id Alarm Interval id to be converted
     *
     * @return the converted object
     */
    @TypeConverter
    fun toAlarmInterval(id: Int?): AlarmInterval? =
        AlarmInterval.values().find { it.id == id }

    /**
     * Converts a Alarm Interval in Int id.
     *
     * @param alarmInterval Alarm Interval to be converted
     *
     * @return the converted object
     */
    @TypeConverter
    fun toId(alarmInterval: AlarmInterval?): Int? =
        alarmInterval?.id
}
