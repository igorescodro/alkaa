package com.escodro.parcelable

/**
 * Annotation to be used in the common code to enable the Parcelable generation in KMP.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class CommonParcelize

/**
 * Interface to be implemented by the classes that are going to be serializable in KMP.
 * On JVM platforms, this will be mapped to java.io.Serializable.
 */
expect interface CommonSerializable

/**
 * Interface to be implemented by the classes that are going to be parcelable and serializable
 * in KMP.
 *
 * This interface inherits from [CommonSerializable] to ensure that any parcelable class
 * can also be handled by state-saving mechanisms that rely on Java Serialization (e.g.
 * when stored in a collection in Compose's rememberSaveable).
 */
expect interface CommonParcelable : CommonSerializable

/**
 * Annotation to be used in the common code to ignore a property from the Parcelable generation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
expect annotation class CommonIgnoredOnParcel()
