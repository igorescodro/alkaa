package com.escodro.test.annotation

import kotlin.test.Ignore

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
actual annotation class IgnoreOnDesktop // Does nothing on iOS

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
actual annotation class IgnoreOnAndroid actual constructor() // Does nothing on iOS

actual typealias IgnoreOnIos = Ignore
