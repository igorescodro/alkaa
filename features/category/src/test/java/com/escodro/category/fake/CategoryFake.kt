package com.escodro.category.fake

import com.escodro.domain.model.Category

val FAKE_DOMAIN_CATEGORY = Category(name = "Books", color = "#444444")

val FAKE_DOMAIN_CATEGORY_LIST = listOf(
    FAKE_DOMAIN_CATEGORY,
    FAKE_DOMAIN_CATEGORY.copy(name = "Movies"),
    FAKE_DOMAIN_CATEGORY.copy(name = "Groceries")
)
