package com.escodro.category.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.escodro.navigation.AlkaaDestinations

/**
 * Category screen navigation module.
 */
val categoryScreenModule = screenModule {
    register<AlkaaDestinations.Category.CategoryBottomSheet> {
        CategoryBottomSheet(categoryId = it.categoryId)
    }
}
