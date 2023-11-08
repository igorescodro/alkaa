package com.escodro.alarm.permission

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

internal class AndroidSdkVersion : SdkVersion {

    @ChecksSdkIntAtLeast(extension = Build.VERSION_CODES.N)
    override val currentVersion: Int = Build.VERSION.SDK_INT
}
