package com.escodro.local

import android.content.ContentValues
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
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class MigrationTest {

    private val allMigrations = arrayOf(MIGRATION_1_2, MIGRATION_2_3)

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
        values.put(FIELD_ID, VALUE_ID)
        values.put(FIELD_IS_COMPLETED, VALUE_IS_COMPLETED)
        values.put(FIELD_TITLE, VALUE_TITLE)
        values.put(FIELD_CATEGORY_ID, VALUE_CATEGORY_ID)
        values.put(FIELD_DESC, VALUE_DESC)
        values.put(FIELD_DUE_DATE, VALUE_CALENDAR)

        helper.createDatabase(TEST_DB, 1).apply {
            insert(TABLE_TASK, SQLiteDatabase.CONFLICT_REPLACE, values)
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2).let {
            val cursor = it.query(SELECT_ALL_FROM_TABLE, arrayOf(VALUE_ID))

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor.moveToFirst())

            Assert.assertEquals(cursor.getIntFromColumn(FIELD_ID), VALUE_ID)
            Assert.assertEquals(cursor.getIntFromColumn(FIELD_IS_COMPLETED), VALUE_IS_COMPLETED)
            Assert.assertEquals(cursor.getStringFromColumn(FIELD_TITLE), VALUE_TITLE)
            Assert.assertEquals(cursor.getStringFromColumn(FIELD_DESC), VALUE_DESC)
            Assert.assertEquals(cursor.getIntFromColumn(FIELD_CATEGORY_ID), VALUE_CATEGORY_ID)
            Assert.assertEquals(cursor.getLongFromColumn(FIELD_DUE_DATE), VALUE_CALENDAR)

            Assert.assertNull(cursor.getIntFromColumn(FIELD_CREATION_DATE))
            Assert.assertNull(cursor.getIntFromColumn(FIELD_COMPLETED_DATE))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate2to3() {
        val values = ContentValues()
        values.put(FIELD_ID, VALUE_ID)
        values.put(FIELD_IS_COMPLETED, VALUE_IS_COMPLETED)
        values.put(FIELD_TITLE, VALUE_TITLE)
        values.put(FIELD_CATEGORY_ID, VALUE_CATEGORY_ID)
        values.put(FIELD_DESC, VALUE_DESC)
        values.put(FIELD_DUE_DATE, VALUE_CALENDAR)
        values.put(FIELD_CREATION_DATE, VALUE_CALENDAR)
        values.put(FIELD_COMPLETED_DATE, VALUE_CALENDAR)

        helper.createDatabase(TEST_DB, 2).apply {
            insert(TABLE_TASK, SQLiteDatabase.CONFLICT_REPLACE, values)
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_1_2, MIGRATION_2_3).let {
            val cursor = it.query(SELECT_ALL_FROM_TABLE, arrayOf(VALUE_ID))

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor.moveToFirst())

            Assert.assertEquals(cursor.getIntFromColumn(FIELD_ID), VALUE_ID)
            Assert.assertEquals(cursor.getIntFromColumn(FIELD_IS_COMPLETED), VALUE_IS_COMPLETED)
            Assert.assertEquals(cursor.getStringFromColumn(FIELD_TITLE), VALUE_TITLE)
            Assert.assertEquals(cursor.getStringFromColumn(FIELD_DESC), VALUE_DESC)
            Assert.assertEquals(cursor.getIntFromColumn(FIELD_CATEGORY_ID), VALUE_CATEGORY_ID)
            Assert.assertEquals(cursor.getLongFromColumn(FIELD_DUE_DATE), VALUE_CALENDAR)
            Assert.assertEquals(cursor.getLongFromColumn(FIELD_CREATION_DATE), VALUE_CALENDAR)
            Assert.assertEquals(cursor.getLongFromColumn(FIELD_COMPLETED_DATE), VALUE_CALENDAR)

            Assert.assertEquals(cursor.getIntFromColumn(FIELD_IS_REPEATING), 0)
            Assert.assertNull(cursor.getIntFromColumn(FIELD_ALARM_INTERVAL))
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

    companion object {
        private const val TEST_DB = "migration-test"
        private const val TABLE_TASK = "Task"

        private const val SELECT_ALL_FROM_TABLE = "SELECT * FROM Task WHERE task_id = ?"

        private const val FIELD_ID = "task_id"
        private const val FIELD_IS_COMPLETED = "task_is_completed"
        private const val FIELD_TITLE = "task_title"
        private const val FIELD_CATEGORY_ID = "task_category_id"
        private const val FIELD_DESC = "task_description"
        private const val FIELD_DUE_DATE = "task_due_date"
        private const val FIELD_CREATION_DATE = "task_creation_date"
        private const val FIELD_COMPLETED_DATE = "task_completed_date"
        private const val FIELD_IS_REPEATING = "task_is_repeating"
        private const val FIELD_ALARM_INTERVAL = "task_alarm_interval"

        private const val VALUE_ID = 15
        private const val VALUE_IS_COMPLETED = 1
        private const val VALUE_TITLE = "Testing the migration"
        private const val VALUE_DESC = "Testing the migration with all the fields"
        private const val VALUE_CATEGORY_ID = 14
        private const val VALUE_CALENDAR = 1_568_209_628_447
    }
}
