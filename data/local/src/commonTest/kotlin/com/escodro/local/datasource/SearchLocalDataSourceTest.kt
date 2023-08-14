package com.escodro.local.datasource

import com.escodro.local.fake.TaskWithCategoryDaoFake
import com.escodro.local.mapper.AlarmIntervalMapper
import com.escodro.local.mapper.CategoryMapper
import com.escodro.local.mapper.TaskMapper
import com.escodro.local.mapper.TaskWithCategoryMapper
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SearchLocalDataSourceTest {

    private val taskWithCategoryDao = TaskWithCategoryDaoFake()

    private val mapper = TaskWithCategoryMapper(TaskMapper(AlarmIntervalMapper()), CategoryMapper())

    private val dataSource = SearchLocalDataSource(taskWithCategoryDao, mapper)

    @BeforeTest
    fun setup() {
        taskWithCategoryDao.clear()
    }

    @Test
    fun `check if the query is enclosed with percent char`() = runTest {
        val query = "name"

        dataSource.findTaskByName(query)

        assertEquals("%$query%", taskWithCategoryDao.searchQuery)
    }
}
