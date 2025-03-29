package com.escodro.parcelable

/**
 * Annotation to be used in the common code to enable the Parcelable generation in KMP.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class CommonParcelize

/**
 * Interface to be implemented by the classes that are going to be parcelable in KMP.
 *
 * See: https://youtrack.jetbrains.com/issue/KT-58892/K2-Parcelize-doesnt-work-in-common-code-when-expect-annotation-is-actualized-with-typealias-to-Parcelize
 * See: https://issuetracker.google.com/issues/315775835#comment16
 */
expect interface CommonParcelable

/**
 * Annotation to be used in the common code to ignore a property from the Parcelable generation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
expect annotation class CommonIgnoredOnParcel()
