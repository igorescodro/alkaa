package com.escodro.test.annotation

import org.junit.Ignore

actual typealias IgnoreOnDesktop = Ignore

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
actual annotation class IgnoreOnAndroid // Does nothing on Desktop

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.CLASS])
actual annotation class IgnoreOnIos // Does nothing on Desktop
