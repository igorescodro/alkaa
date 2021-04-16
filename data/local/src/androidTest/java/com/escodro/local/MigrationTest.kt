package com.escodro.local

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.core.extension.getIntFromColumn
import com.escodro.core.extension.getLongFromColumn
import com.escodro.core.extension.getStringFromColumn
import com.escodro.local.migration.MIGRATION_1_2
import com.escodro.local.migration.MIGRATION_2_3
import com.escodro.local.migration.MIGRATION_3_4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class MigrationTest {

    private val allMigrations = arrayOf(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TaskDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        val values = ContentValues()
        values.put(Task.FIELD_ID, Task.VALUE_ID)
        values.put(Task.FIELD_IS_COMPLETED, Task.VALUE_IS_COMPLETED)
        values.put(Task.FIELD_TITLE, Task.VALUE_TITLE)
        values.put(Task.FIELD_CATEGORY_ID, Task.VALUE_CATEGORY_ID)
        values.put(Task.FIELD_DESC, Task.VALUE_DESC)
        values.put(Task.FIELD_DUE_DATE, Task.VALUE_CALENDAR)

        helper.createDatabase(TEST_DB, 1).apply {
            insert(Task.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2).let {
            val cursor = it.query(Task.SELECT_ALL_FROM_TABLE, arrayOf(Task.VALUE_ID))

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor.moveToFirst())

            cursor.run {
                assertFromColumn(Task.FIELD_ID, Task.VALUE_ID)
                assertFromColumn(Task.FIELD_IS_COMPLETED, Task.VALUE_IS_COMPLETED)
                assertFromColumn(Task.FIELD_TITLE, Task.VALUE_TITLE)
                assertFromColumn(Task.FIELD_DESC, Task.VALUE_DESC)
                assertFromColumn(Task.FIELD_CATEGORY_ID, Task.VALUE_CATEGORY_ID)
                assertFromColumn(Task.FIELD_DUE_DATE, Task.VALUE_CALENDAR)

                Assert.assertNull(cursor.getIntFromColumn(Task.FIELD_CREATION_DATE))
                Assert.assertNull(cursor.getIntFromColumn(Task.FIELD_COMPLETED_DATE))
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate2to3() {
        val values = ContentValues()
        values.put(Task.FIELD_ID, Task.VALUE_ID)
        values.put(Task.FIELD_IS_COMPLETED, Task.VALUE_IS_COMPLETED)
        values.put(Task.FIELD_TITLE, Task.VALUE_TITLE)
        values.put(Task.FIELD_CATEGORY_ID, Task.VALUE_CATEGORY_ID)
        values.put(Task.FIELD_DESC, Task.VALUE_DESC)
        values.put(Task.FIELD_DUE_DATE, Task.VALUE_CALENDAR)
        values.put(Task.FIELD_CREATION_DATE, Task.VALUE_CALENDAR)
        values.put(Task.FIELD_COMPLETED_DATE, Task.VALUE_CALENDAR)

        helper.createDatabase(TEST_DB, 2).apply {
            insert(Task.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_1_2, MIGRATION_2_3).let {
            val cursor = it.query(Task.SELECT_ALL_FROM_TABLE, arrayOf(Task.VALUE_ID))

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor.moveToFirst())

            cursor.run {
                assertFromColumn(Task.FIELD_ID, Task.VALUE_ID)
                assertFromColumn(Task.FIELD_IS_COMPLETED, Task.VALUE_IS_COMPLETED)
                assertFromColumn(Task.FIELD_TITLE, Task.VALUE_TITLE)
                assertFromColumn(Task.FIELD_DESC, Task.VALUE_DESC)
                assertFromColumn(Task.FIELD_CATEGORY_ID, Task.VALUE_CATEGORY_ID)
                assertFromColumn(Task.FIELD_DUE_DATE, Task.VALUE_CALENDAR)
                assertFromColumn(Task.FIELD_CREATION_DATE, Task.VALUE_CALENDAR)
                assertFromColumn(Task.FIELD_COMPLETED_DATE, Task.VALUE_CALENDAR)

                Assert.assertEquals(cursor.getIntFromColumn(Task.FIELD_IS_REPEATING), 0)
                Assert.assertNull(cursor.getIntFromColumn(Task.FIELD_ALARM_INTERVAL))
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate3to4() {
        val values = ContentValues()
        values.put(Category.FIELD_ID, Category.VALUE_ID)
        values.put(Category.FIELD_NAME, Category.VALUE_NAME)
        values.put(Category.FIELD_COLOR, Category.VALUE_COLOR)

        helper.createDatabase(TEST_DB, 3).apply {
            insert(Category.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 3, true).let {
            val cursor = it.query(Category.SELECT_ALL_FROM_TABLE, arrayOf(Category.VALUE_ID))

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor.moveToFirst())

            cursor.run {
                assertFromColumn(Category.FIELD_ID, Category.VALUE_ID)
                assertFromColumn(Category.FIELD_NAME, Category.VALUE_NAME)
                assertFromColumn(Category.FIELD_COLOR, Category.VALUE_COLOR)
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        val db = helper.createDatabase(TEST_DB, 1)
        db.close()

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            TaskDatabase::class.java,
            TEST_DB
        ).addMigrations(*allMigrations).build().apply {
            openHelper.writableDatabase
            close()
        }
    }

    private fun Cursor.assertFromColumn(field: String, fieldValue: Long) =
        Assert.assertEquals(getLongFromColumn(field), fieldValue)

    private fun Cursor.assertFromColumn(field: String, fieldValue: String) =
        Assert.assertEquals(getStringFromColumn(field), fieldValue)

    private fun Cursor.assertFromColumn(field: String, fieldValue: Int) =
        Assert.assertEquals(getIntFromColumn(field), fieldValue)

    companion object {
        private const val TEST_DB = "migration-test"

        private object Task {
            const val TABLE_NAME = "Task"

            const val SELECT_ALL_FROM_TABLE = "SELECT * FROM Task WHERE task_id = ?"

            const val FIELD_ID = "task_id"
            const val FIELD_IS_COMPLETED = "task_is_completed"
            const val FIELD_TITLE = "task_title"
            const val FIELD_CATEGORY_ID = "task_category_id"
            const val FIELD_DESC = "task_description"
            const val FIELD_DUE_DATE = "task_due_date"
            const val FIELD_CREATION_DATE = "task_creation_date"
            const val FIELD_COMPLETED_DATE = "task_completed_date"
            const val FIELD_IS_REPEATING = "task_is_repeating"
            const val FIELD_ALARM_INTERVAL = "task_alarm_interval"

            const val VALUE_ID = 15
            const val VALUE_IS_COMPLETED = 1
            const val VALUE_TITLE = "Testing the migration"
            const val VALUE_DESC = "Testing the migration with all the fields"
            const val VALUE_CATEGORY_ID = 14
            const val VALUE_CALENDAR = 1_568_209_628_447
        }

        private object Category {
            const val TABLE_NAME = "Category"

            const val SELECT_ALL_FROM_TABLE = "SELECT * FROM Category WHERE category_id = ?"

            const val FIELD_ID = "category_id"
            const val FIELD_NAME = "category_name"
            const val FIELD_COLOR = "category_color"

            const val VALUE_ID = 8
            const val VALUE_NAME = "Movies"
            const val VALUE_COLOR = "#FF8800"
        }
    }
}
