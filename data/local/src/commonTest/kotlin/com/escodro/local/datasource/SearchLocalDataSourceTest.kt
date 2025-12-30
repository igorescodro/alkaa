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

    @Suppress("IgnoredReturnValue")
    @Test
    fun test_if_the_query_is_enclosed_with_percent_char() {
        runTest {
            val query = "name"

            dataSource.findTaskByName(query)

            assertEquals(expected = "%$query%", actual = taskWithCategoryDao.searchQuery)
        }
    }
}
