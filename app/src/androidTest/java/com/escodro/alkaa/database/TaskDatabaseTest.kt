package com.escodro.alkaa.database

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.escodro.alkaa.data.local.TaskDatabase
import com.escodro.alkaa.data.local.dao.CategoryDao
import com.escodro.alkaa.data.local.dao.TaskDao
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import junit.framework.Assert.assertTrue
import org.junit.After
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
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = database.taskDao()

        categoryDao = database.categoryDao()
        categoryDao.insertCategory(Category("Work", "#cc5a71"))
        categoryDao.insertCategory(Category("Personal", "#58a4b0"))
        categoryDao.insertCategory(Category("Family", "#519872"))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertTaskAndReadInList() {
        val task = Task(title = TASK_NAME)
        taskDao.insertTask(task)

        val list = taskDao.getAllTasks().blockingFirst()
        assertTrue(list.contains(task))
    }

    @Test
    fun insertAndUpdateTask() {
        val task = Task(title = TASK_NAME)
        taskDao.insertTask(task)

        val list = taskDao.getAllTasks().blockingFirst()
        val updatedTask = list[0]
        updatedTask.title = "call Martha"
        taskDao.updateTask(updatedTask)

        val updatedList = taskDao.getAllTasks().blockingFirst()
        assertTrue(updatedList.contains(updatedTask))
    }

    @Test
    fun insertAndAddCategoryInTask() {
        val task = Task(title = TASK_NAME)
        task.categoryId = categoryDao.getAllCategories().blockingFirst()[0].id
        taskDao.insertTask(task)

        val taskWithCategory = taskDao.getAllTasksWithCategory().blockingFirst()[0]
        assertTrue(taskWithCategory.task == task)
    }

    @Test
    fun validateDateConverter() {
        val taskName = "Take medicine"
        val task = Task(title = taskName)

        val calendar = Calendar.getInstance()
        calendar.set(2018, 3, 15, 16, 1)
        task.dueDate = calendar

        taskDao.insertTask(task)

        val selectedDate = taskDao.findTaskByDescription(taskName).blockingGet().dueDate

        assertTrue(selectedDate?.get(Calendar.YEAR) == 2018)
        assertTrue(selectedDate?.get(Calendar.MONTH) == 3)
        assertTrue(selectedDate?.get(Calendar.DATE) == 15)
        assertTrue(selectedDate?.get(Calendar.HOUR_OF_DAY) == 16)
        assertTrue(selectedDate?.get(Calendar.MINUTE) == 1)
    }

    companion object {

        private const val TASK_NAME = "call John"
    }
}
