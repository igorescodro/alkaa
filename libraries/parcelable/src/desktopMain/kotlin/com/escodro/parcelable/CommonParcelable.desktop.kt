package com.escodro.parcelable

import java.io.Serializable

actual typealias CommonSerializable = Serializable

actual interface CommonParcelable : CommonSerializable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
actual annotation class CommonIgnoredOnParcel
