package com.escodro.resources.provider

import dev.icerock.moko.resources.desc.StringDesc

/**
 * Provides resources by each platform.
 */
expect class ResourcesProvider constructor() {

    /**
     * Get the string from the provided [StringDesc].
     */
    fun getString(stringDesc: StringDesc): String
}
