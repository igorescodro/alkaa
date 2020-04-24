package com.escodro.local.datasource

import com.escodro.local.mapper.TaskWithCategoryMapper
import com.escodro.local.provider.DaoProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

internal class SearchLocalDataSourceTest {

    private val mockDaoProvider = mockk<DaoProvider>(relaxed = true)

    private val mockMapper = mockk<TaskWithCategoryMapper>(relaxed = true)

    private val dataSource = SearchLocalDataSource(mockDaoProvider, mockMapper)

    @Before
    fun setup() {
        coEvery { mockDaoProvider.getTaskWithCategoryDao().findTaskByName(any()) } returns listOf()
    }

    @Test
    fun `check if the query is enclosed with %`() = runBlockingTest {
        val query = "name"
        dataSource.findTaskByName(query)
        coVerify { mockDaoProvider.getTaskWithCategoryDao().findTaskByName("%$query%") }
    }
}
