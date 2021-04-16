package com.escodro.repository.mapper

import com.escodro.repository.model.AlarmInterval.DAILY
import com.escodro.repository.model.AlarmInterval.HOURLY
import com.escodro.repository.model.AlarmInterval.MONTHLY
import com.escodro.repository.model.AlarmInterval.WEEKLY
import com.escodro.repository.model.AlarmInterval.YEARLY
import com.escodro.domain.model.AlarmInterval as DomainInterval
import com.escodro.repository.model.AlarmInterval as RepoInterval

/**
 * Maps Alarm Interval between Repository and Domain.
 */
internal class AlarmIntervalMapper {

    /**
     * Maps Alarm Interval from Repo to Domain.
     *
     * @param alarmInterval the object to be converted
     *
     * @return the converted object
     */
    fun toDomain(alarmInterval: RepoInterval): DomainInterval =
        when (alarmInterval) {
            HOURLY -> DomainInterval.HOURLY
            DAILY -> DomainInterval.DAILY
            WEEKLY -> DomainInterval.WEEKLY
            MONTHLY -> DomainInterval.MONTHLY
            YEARLY -> DomainInterval.YEARLY
        }

    /**
     * Maps Alarm Interval from Domain to Repo.
     *
     * @param alarmInterval the object to be converted
     *
     * @return the converted object
     */
    fun toRepo(alarmInterval: DomainInterval): RepoInterval =
        when (alarmInterval) {
            DomainInterval.HOURLY -> HOURLY
            DomainInterval.DAILY -> DAILY
            DomainInterval.WEEKLY -> WEEKLY
            DomainInterval.MONTHLY -> MONTHLY
            DomainInterval.YEARLY -> YEARLY
        }
}
