package com.escodro.test

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Implementation for JUnit [TestRule] interface.
 */
class ImmediateSchedulerRule : TestRule {

    /**
     * Implementation for [TestRule.apply] method.
     */
    override fun apply(base: Statement, d: Description) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

            try {
                base.evaluate()
            } finally {
                RxJavaPlugins.reset()
                RxAndroidPlugins.reset()
            }
        }
    }
}
