package com.escodro.preference.provider

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

internal class AndroidAppInfoProvider(private val context: Context) : AppInfoProvider {

    override fun getAppVersion(): String = with(context) {
        var packageInfo: PackageInfo?
        packageName.let {
            packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(it, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(it, 0)
            }
        }
        return packageInfo?.versionName ?: INVALID_VERSION
    }

    private companion object {
        private const val INVALID_VERSION = "x.x.x"
    }
}
