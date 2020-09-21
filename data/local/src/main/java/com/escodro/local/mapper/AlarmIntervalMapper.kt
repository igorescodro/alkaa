package com.escodro.local.mapper

import com.escodro.local.model.AlarmInterval.DAILY
import com.escodro.local.model.AlarmInterval.HOURLY
import com.escodro.local.model.AlarmInterval.MONTHLY
import com.escodro.local.model.AlarmInterval.WEEKLY
import com.escodro.local.model.AlarmInterval.YEARLY
import com.escodro.local.model.AlarmInterval as LocalInterval
import com.escodro.repository.model.AlarmInterval as RepoInterval

/**
 * Maps Alarm Interval between Repository and Domain.
 */
internal class AlarmIntervalMapper {

    /**
     * Maps Alarm Interval from Local to Repo.
     *
     * @param alarmInterval the object to be converted
     *
     * @return the converted object
     */
    fun toRepo(alarmInterval: LocalInterval): RepoInterval =
        when (alarmInterval) {
            HOURLY -> RepoInterval.HOURLY
            DAILY -> RepoInterval.DAILY
            WEEKLY -> RepoInterval.WEEKLY
            MONTHLY -> RepoInterval.MONTHLY
            YEARLY -> RepoInterval.YEARLY
        }

    /**
     * Maps Alarm Interval from Repo to Local.
     *
     * @param alarmInterval the object to be converted
     *
     * @return the converted object
     */
    fun toLocal(alarmInterval: RepoInterval): LocalInterval =
        when (alarmInterval) {
            RepoInterval.HOURLY -> HOURLY
            RepoInterval.DAILY -> DAILY
            RepoInterval.WEEKLY -> WEEKLY
            RepoInterval.MONTHLY -> MONTHLY
            RepoInterval.YEARLY -> YEARLY
        }
}
