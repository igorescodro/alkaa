package com.escodro.test.annotation

import org.junit.Ignore

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
actual annotation class IgnoreOnDesktop // Does nothing on Android

actual typealias IgnoreOnAndroid = Ignore

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
actual annotation class IgnoreOnIos // Does nothing on Android
