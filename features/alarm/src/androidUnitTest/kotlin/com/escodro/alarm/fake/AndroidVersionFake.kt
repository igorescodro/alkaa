package com.escodro.alarm.fake

import com.escodro.alarm.permission.SdkVersion

internal class AndroidVersionFake : SdkVersion {

    var version: Int = 0

    override val currentVersion: Int
        get() = version

    fun clean() {
        version = 0
    }
}
