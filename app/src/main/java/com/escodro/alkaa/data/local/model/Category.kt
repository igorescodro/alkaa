package com.escodro.alkaa.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 *
 * [Entity] to represent a category.
 *
 * @author Igor Escodro on 1/2/18.
 */
@Parcelize
@Entity
data class Category(
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "color") var color: String?
) : Parcelable {

    @IgnoredOnParcel
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
