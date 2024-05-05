package com.escodro.resources.provider

import dev.icerock.moko.resources.desc.StringDesc

actual class ResourcesProvider actual constructor() {

    actual fun getString(stringDesc: StringDesc): String =
        stringDesc.localized()
}
