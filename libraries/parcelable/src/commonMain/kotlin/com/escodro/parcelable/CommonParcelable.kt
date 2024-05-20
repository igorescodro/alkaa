package com.escodro.parcelable

/**
 * Annotation to be used in the common code to enable the Parcelable generation in KMP.
 */
annotation class CommonParcelize

/**
 * Interface to be implemented by the classes that are going to be parcelable in KMP.
 *
 * See: https://youtrack.jetbrains.com/issue/KT-58892/K2-Parcelize-doesnt-work-in-common-code-when-expect-annotation-is-actualized-with-typealias-to-Parcelize
 * See: https://issuetracker.google.com/issues/315775835#comment16
 */
expect interface CommonParcelable
