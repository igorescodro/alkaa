package com.escodro.alkaa.common.extension

import android.os.Handler

/**
 * Runs a code block with a given delay.
 *
 * @param delay the delay (in milliseconds) until the code block will be executed
 * @param func the function to be executed
 */
fun withDelay(delay: Long, func: () -> Unit) {
    Handler().postDelayed({ func() }, delay)
}
