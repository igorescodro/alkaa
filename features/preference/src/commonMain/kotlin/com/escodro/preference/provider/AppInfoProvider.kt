package com.escodro.preference.provider

/**
 * Provides the app information on each platform.
 */
internal interface AppInfoProvider {

    /**
     * Returns the app version.
     */
    fun getAppVersion(): String
}
