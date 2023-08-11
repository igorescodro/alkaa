package com.escodro.local.datasource

import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.mapper.TaskWithCategoryMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class SearchLocalDataSourceTest {

    private val mockTaskWithCategoryDao = mockk<TaskWithCategoryDao>(relaxed = true)

    private val mockMapper = mockk<TaskWithCategoryMapper>(relaxed = true)

    private val dataSource = SearchLocalDataSource(mockTaskWithCategoryDao, mockMapper)

    @Before
    fun setup() {
        coEvery { mockTaskWithCategoryDao.findTaskByName(any()) } returns flow { }
    }

    @Test
    fun `check if the query is enclosed with percent char`() = runTest {
        val query = "name"
        dataSource.findTaskByName(query)
        coVerify { mockTaskWithCategoryDao.findTaskByName("%$query%") }
    }
}
