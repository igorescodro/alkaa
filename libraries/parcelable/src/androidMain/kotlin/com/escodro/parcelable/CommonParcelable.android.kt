package com.escodro.parcelable

import java.io.Serializable

actual typealias CommonSerializable = Serializable

actual interface CommonParcelable : android.os.Parcelable, CommonSerializable

actual typealias CommonIgnoredOnParcel = kotlinx.parcelize.IgnoredOnParcel
