package com.escodro.category.event

import com.escodro.navigationapi.destination.CategoryDestination
import com.escodro.navigationapi.event.CategoryEvent
import kotlin.test.Test
import kotlin.test.assertIs

internal class CategoryEventTest {

    @Test
    fun `test if on category details click returns category details destination`() {
        // Given
        val event = CategoryEvent.OnCategoryDetailsClick(categoryId = 42L)

        // When
        val destination = event.nextDestination()

        // Then
        assertIs<CategoryDestination.CategoryDetails>(destination)
    }

    @Test
    fun `test if on category click returns category bottom sheet destination`() {
        // Given
        val event = CategoryEvent.OnCategoryClick(categoryId = 42L)

        // When
        val destination = event.nextDestination()

        // Then
        assertIs<CategoryDestination.CategoryBottomSheet>(destination)
    }
}
