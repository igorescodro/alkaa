package com.escodro.test.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTestDispatcherImpl(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : CoroutinesTestDispatcher {

    override fun testDispatcher(): TestDispatcher = dispatcher

    @BeforeTest
    override fun starting() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    override fun finished() {
        Dispatchers.resetMain()
    }
}
