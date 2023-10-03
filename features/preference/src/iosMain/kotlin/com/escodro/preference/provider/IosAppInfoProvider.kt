package com.escodro.preference.provider

import platform.Foundation.NSBundle

internal class IosAppInfoProvider : AppInfoProvider {

    override fun getAppVersion(): String =
        NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as String?
            ?: INVALID_VERSION

    private companion object {
        private const val INVALID_VERSION = "x.x.x"
    }
}
