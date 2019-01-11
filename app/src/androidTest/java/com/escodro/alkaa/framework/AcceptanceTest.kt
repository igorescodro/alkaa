package com.escodro.alkaa.framework

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
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

    val context: Context = ApplicationProvider.getApplicationContext()

    val checkThat: Checkers = Checkers()

    val events: Events = Events()

    val daoProvider: DaoProvider by inject()
}
