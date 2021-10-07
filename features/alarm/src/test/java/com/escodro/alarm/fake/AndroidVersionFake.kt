package com.escodro.alarm.fake

import com.escodro.alarm.permission.AndroidVersion

internal class AndroidVersionFake : AndroidVersion {

    var version: Int = 0

    override val currentVersion: Int
        get() = version
}
