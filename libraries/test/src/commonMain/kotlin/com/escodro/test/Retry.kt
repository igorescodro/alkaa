package com.escodro.test

/**
 * Executes the [block] up to [times] times until it succeeds.
 *
 * @param times the maximum number of times the block should be executed
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
