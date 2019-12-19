package com.escodro.alkaa.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.escodro.local.TaskDatabase
import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.local.dao.TaskWithCategoryDao
import com.escodro.local.model.Category
import com.escodro.local.model.Task
import java.util.Calendar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest {

    private lateinit var database: TaskDatabase

    private lateinit var taskDao: TaskDao

    private lateinit var taskWithCategoryDao: TaskWithCategoryDao

    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() = runBlocking {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = database.taskDao()
        taskWithCategoryDao = database.taskWithCategoryDao()
        categoryDao = database.categoryDao()

        categoryDao.insertCategory(Category(name = "Work", color = "#cc5a71"))
        categoryDao.insertCategory(Category(name = "Personal", color = "#58a4b0"))
        categoryDao.insertCategory(Category(name = "Family", color = "#519872"))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertTaskAndReadInList() {
        val task = Task(id = 14, title = TASK_NAME)
        taskDao.insertTask(task).blockingGet()

        val list = taskDao.findAllTasks().blockingFirst()
        assertTrue(list.contains(task))
    }

    @Test
    fun insertTaskWithDescription() {
        val task = Task(title = TASK_NAME, description = TASK_DESCRIPTION)
        taskDao.insertTask(task).blockingGet()

        val taskDescription = taskDao.findTaskByTitle(TASK_NAME).blockingGet().description
        assertTrue(taskDescription == TASK_DESCRIPTION)
    }

    @Test
    fun insertAndUpdateTask() {
        val task = Task(title = TASK_NAME)
        taskDao.insertTask(task).blockingGet()

        val list = taskDao.findAllTasks().blockingFirst()
        val updatedTask = list[0]
        updatedTask.title = "call Martha"
        updatedTask.description = TASK_DESCRIPTION
        taskDao.updateTask(updatedTask).blockingGet()

        val updatedList = taskDao.findAllTasks().blockingFirst()
        assertTrue(updatedList.contains(updatedTask))
    }

    @Test
    fun insertAndAddCategoryInTask() = runBlocking {
        val task = Task(id = 16, title = TASK_NAME)
        task.categoryId = categoryDao.findAllCategories().first()[0].id
        taskDao.insertTask(task).blockingGet()

        val taskWithCategory = taskWithCategoryDao.findAllTasksWithCategory().blockingFirst()[0]
        assertTrue(taskWithCategory.task == task)
    }

    @Test
    fun clearTaskFields() = runBlocking {
        val task = Task(title = TASK_NAME).apply {
            categoryId = categoryDao.findAllCategories().first()[0].id
            description = TASK_DESCRIPTION
            dueDate = Calendar.getInstance()
        }
        taskDao.insertTask(task).blockingGet()

        val clearTask = taskDao.findTaskByTitle(TASK_NAME).blockingGet()
        clearTask.apply {
            categoryId = null
            description = null
            dueDate = null
        }
        taskDao.updateTask(clearTask).blockingGet()

        val updatedList = taskDao.findAllTasks().blockingFirst()
        assertTrue(updatedList.contains(clearTask))
    }

    @Test
    fun validateDateConverter() {
        val taskName = "Take medicine"
        val task = Task(title = taskName)

        val calendar = Calendar.getInstance()
        calendar.set(2018, 3, 15, 16, 1)
        task.dueDate = calendar

        taskDao.insertTask(task).blockingGet()

        val selectedDate = taskDao.findTaskByTitle(taskName).blockingGet().dueDate

        assertTrue(selectedDate?.get(Calendar.YEAR) == 2018)
        assertTrue(selectedDate?.get(Calendar.MONTH) == 3)
        assertTrue(selectedDate?.get(Calendar.DATE) == 15)
        assertTrue(selectedDate?.get(Calendar.HOUR_OF_DAY) == 16)
        assertTrue(selectedDate?.get(Calendar.MINUTE) == 1)
    }

    companion object {

        private const val TASK_NAME = "call John"

        private const val TASK_DESCRIPTION = "He is busy until 8PM. +55 19 6489 5456"
    }
}
