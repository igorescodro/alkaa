package com.escodro.task.mapper

import com.escodro.domain.model.AlarmInterval as DomainInterval
import com.escodro.task.model.AlarmInterval as ViewDataInterval
import com.escodro.task.model.AlarmInterval.DAILY
import com.escodro.task.model.AlarmInterval.HOURLY
import com.escodro.task.model.AlarmInterval.MONTHLY
import com.escodro.task.model.AlarmInterval.WEEKLY
import com.escodro.task.model.AlarmInterval.YEARLY

/**
 * Maps Alarm Interval between Domain and View.
 */
internal class AlarmIntervalMapper {

    /**
     * Maps Alarm Interval from View Data to Domain.
     *
     * @param alarmInterval the object to be converted
     *
     * @return the converted object
     */
    fun toRepo(alarmInterval: ViewDataInterval): DomainInterval =
        when (alarmInterval) {
            HOURLY -> DomainInterval.HOURLY
            DAILY -> DomainInterval.DAILY
            WEEKLY -> DomainInterval.WEEKLY
            MONTHLY -> DomainInterval.MONTHLY
            YEARLY -> DomainInterval.YEARLY
        }

    /**
     * Maps Alarm Interval from Domain to View Data.
     *
     * @param alarmInterval the object to be converted
     *
     * @return the converted object
     */
    fun toViewData(alarmInterval: DomainInterval): ViewDataInterval =
        when (alarmInterval) {
            DomainInterval.HOURLY -> HOURLY
            DomainInterval.DAILY -> DAILY
            DomainInterval.WEEKLY -> WEEKLY
            DomainInterval.MONTHLY -> MONTHLY
            DomainInterval.YEARLY -> YEARLY
        }
}
