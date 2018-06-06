package com.escodro.alkaa.framework

import android.app.Activity
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.escodro.alkaa.di.DaoRepository
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.standalone.inject
import org.koin.test.KoinTest

/**
 * Class to wrap the test intents.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class AcceptanceTest<T : Activity>(clazz: Class<T>) : KoinTest {

    @Rule
    @JvmField
    val testRule: ActivityTestRule<T> = IntentsTestRule(clazz)

    val context: Context = InstrumentationRegistry.getTargetContext()

    val checkThat: Matchers = Matchers()

    val events: Events = Events()

    val daoRepository: DaoRepository by inject()
}
