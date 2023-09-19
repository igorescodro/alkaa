package com.escodro.test.rule

import kotlinx.coroutines.test.TestDispatcher
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Temporary interface to simulate a `TestRule` for changing the [TestDispatcher] in the tests.
 * This should be removed when the `TestRule` is available in KMP/KotlinX Test
 */
interface CoroutinesTestDispatcher {

    /**
     * Returns the [TestDispatcher] to be used in the tests.
     */
    fun testDispatcher(): TestDispatcher

    /**
     * Starts the [TestDispatcher] to be used in the tests.
     */
    @BeforeTest
    fun starting()

    /**
     * Finishes the [TestDispatcher] to be used in the tests.
     */
    @AfterTest
    fun finished()
}
