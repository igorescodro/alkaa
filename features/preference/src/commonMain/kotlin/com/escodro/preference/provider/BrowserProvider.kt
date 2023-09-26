package com.escodro.preference.provider

/**
 * Provides the browser provider on each platform.
 */
internal interface BrowserProvider {

    /**
     * Opens the given [url] on the browser.
     *
     * @param url the url to be opened
     */
    fun openUrl(url: String)
}
