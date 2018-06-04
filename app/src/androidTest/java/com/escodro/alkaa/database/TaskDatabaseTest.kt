package com.escodro.alkaa.database

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.escodro.alkaa.data.local.TaskDatabase
import com.escodro.alkaa.data.local.dao.TaskDao
import com.escodro.alkaa.data.local.model.Task
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest {

    private lateinit var database: TaskDatabase

    private lateinit var taskDao: TaskDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        taskDao = database.taskDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertTaskAndReadInList() {
        val task = Task(false, TASK_NAME)
        taskDao.insertTask(task)

        val list = taskDao.getAllTasks().blockingFirst()
        assert(list.contains(task))
    }

    @Test
    fun insertAndUpdateTask() {
        val task = Task(false, TASK_NAME)
        taskDao.insertTask(task)

        val list = taskDao.getAllTasks().blockingFirst()
        val updatedTask = list[0]
        updatedTask.description = "call Martha"
        taskDao.updateTask(updatedTask)

        val updatedList = taskDao.getAllTasks().blockingFirst()
        assert(updatedList.contains(updatedTask))
    }

    companion object {

        private const val TASK_NAME = "call John"
    }
}
