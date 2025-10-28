package com.escodro.parcelable

actual interface CommonParcelable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
actual annotation class CommonIgnoredOnParcel
