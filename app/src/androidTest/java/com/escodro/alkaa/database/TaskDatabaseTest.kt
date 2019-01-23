package com.escodro.alkaa.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.escodro.alkaa.data.local.TaskDatabase
import com.escodro.alkaa.data.local.dao.CategoryDao
import com.escodro.alkaa.data.local.dao.TaskDao
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest {

    private lateinit var database: TaskDatabase

    private lateinit var taskDao: TaskDao

    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = database.taskDao()

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
        taskDao.insertTask(task)

        val list = taskDao.getAllTasks().blockingFirst()
        assertTrue(list.contains(task))
    }

    @Test
    fun insertTaskWithDescription() {
        val task = Task(title = TASK_NAME, description = TASK_DESCRIPTION)
        taskDao.insertTask(task)

        val taskDescription = taskDao.findTaskByTitle(TASK_NAME).blockingGet().description
        assertTrue(taskDescription == TASK_DESCRIPTION)
    }

    @Test
    fun insertAndUpdateTask() {
        val task = Task(title = TASK_NAME)
        taskDao.insertTask(task)

        val list = taskDao.getAllTasks().blockingFirst()
        val updatedTask = list[0]
        updatedTask.title = "call Martha"
        updatedTask.description = TASK_DESCRIPTION
        taskDao.updateTask(updatedTask)

        val updatedList = taskDao.getAllTasks().blockingFirst()
        assertTrue(updatedList.contains(updatedTask))
    }

    @Test
    fun insertAndAddCategoryInTask() {
        val task = Task(id = 16, title = TASK_NAME)
        task.categoryId = categoryDao.getAllCategories().blockingFirst()[0].id
        taskDao.insertTask(task)

        val taskWithCategory = taskDao.getAllTasksWithCategory(false).blockingFirst()[0]
        assertTrue(taskWithCategory.task == task)
    }

    @Test
    fun clearTaskFields() {
        val task = Task(title = TASK_NAME).apply {
            categoryId = categoryDao.getAllCategories().blockingFirst()[0].id
            description = TASK_DESCRIPTION
            dueDate = Calendar.getInstance()
        }
        taskDao.insertTask(task)

        val clearTask = taskDao.findTaskByTitle(TASK_NAME).blockingGet()
        clearTask.apply {
            categoryId = null
            description = null
            dueDate = null
        }
        taskDao.updateTask(clearTask)

        val updatedList = taskDao.getAllTasks().blockingFirst()
        assertTrue(updatedList.contains(clearTask))
    }

    @Test
    fun validateDateConverter() {
        val taskName = "Take medicine"
        val task = Task(title = taskName)

        val calendar = Calendar.getInstance()
        calendar.set(2018, 3, 15, 16, 1)
        task.dueDate = calendar

        taskDao.insertTask(task)

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
