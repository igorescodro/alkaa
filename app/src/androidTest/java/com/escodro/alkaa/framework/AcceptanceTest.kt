package com.escodro.alkaa.framework

import android.app.Activity
import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.escodro.alkaa.di.provider.DaoProvider
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

    val mDaoProvider: DaoProvider by inject()
}
