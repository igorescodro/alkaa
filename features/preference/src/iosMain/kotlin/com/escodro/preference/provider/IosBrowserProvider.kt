package com.escodro.preference.provider

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class IosBrowserProvider : BrowserProvider {

    override fun openUrl(url: String) {
        val nsurl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsurl)
    }
}
