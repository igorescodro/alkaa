package com.escodro.domain.usecase.search

import com.escodro.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

internal class SearchTasksByNameTest {

    private val mockRepo = mockk<SearchRepository>()

    private val searchTasks = SearchTasksByName(mockRepo)

    @Before
    fun setup() {
        coEvery { mockRepo.findTaskByName(any()) } returns mockk()
    }

    @Test
    fun `check if the repository is called`() = runBlockingTest {
        val query = "name"
        searchTasks(query)
        coVerify { mockRepo.findTaskByName(query) }
    }
}
