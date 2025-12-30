package com.escodro.test

/**
 * Executes the [block] and retries it [times] if it fails.
 *
 * @param times the number of times the block should be retried
 * @param block the block to be executed
 */
@Suppress("ForbiddenMethodCall")
fun retry(times: Int, block: () -> Unit) {
    var caughtThrowable: Throwable? = null

    for (i in 0 until times) {
        try {
            block()
            return
        } catch (t: Throwable) {
            caughtThrowable = t
            println("[DEBUG_LOG] Test failed, retry ${i + 1}/$times")
        }
    }
    throw caughtThrowable!!
}
