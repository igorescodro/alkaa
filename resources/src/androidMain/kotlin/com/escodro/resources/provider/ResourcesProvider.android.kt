package com.escodro.resources.provider

import android.content.Context
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class ResourcesProvider actual constructor() : KoinComponent {

    private val context: Context by inject()

    actual fun getString(stringDesc: StringDesc): String =
        stringDesc.toString(context = context)
}
