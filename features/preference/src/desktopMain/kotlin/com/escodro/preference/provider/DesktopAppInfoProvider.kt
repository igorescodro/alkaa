package com.escodro.preference.provider

internal class DesktopAppInfoProvider : AppInfoProvider {

    override fun getAppVersion(): String {
        val version = System.getProperty("packageVersion") ?: "0.0.0"
        return "$version-alpha"
    }
}
