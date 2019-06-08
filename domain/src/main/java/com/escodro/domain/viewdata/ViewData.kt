package com.escodro.domain.viewdata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Encapsulates the UI representation of database models.
 */
sealed class ViewData {

    /**
     * UI representation of a Category.
     */
    @Parcelize
    data class Category(var id: Long = 0, var name: String?, var color: String?) : Parcelable
}
