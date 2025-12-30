package com.escodro.test.annotation

/**
 * Annotation to ignore tests on the Android platform.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
expect annotation class IgnoreOnAndroid()

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
expect annotation class IgnoreOnIos()

/**
 * Annotation to ignore tests on Desktop platforms.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
expect annotation class IgnoreOnDesktop()
