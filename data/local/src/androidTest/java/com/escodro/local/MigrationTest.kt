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
import java.io.IOException
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MigrationTest {

    private val allMigrations = arrayOf(MIGRATION_1_2)

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
        values.put("task_id", FIELD_ID)
        values.put("task_is_completed", FIELD_IS_COMPLETED)
        values.put("task_title", FIELD_TITLE)
        values.put("task_category_id", FIELD_CATEGORY_ID)
        values.put("task_description", FIELD_DESC)
        values.put("task_due_date", FIELD_DUE_DATE)

        helper.createDatabase(TEST_DB, 1).apply {
            insert("Task", SQLiteDatabase.CONFLICT_REPLACE, values)
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2).let {
            val cursor = it.query("SELECT * FROM Task WHERE task_id = ?", arrayOf(FIELD_ID))

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor.moveToFirst())

            Assert.assertEquals(cursor.getIntFromColumn("task_id"), FIELD_ID)
            Assert.assertEquals(cursor.getIntFromColumn("task_is_completed"), FIELD_IS_COMPLETED)
            Assert.assertEquals(cursor.getStringFromColumn("task_title"), FIELD_TITLE)
            Assert.assertEquals(cursor.getStringFromColumn("task_description"), FIELD_DESC)
            Assert.assertEquals(cursor.getIntFromColumn("task_category_id"), FIELD_CATEGORY_ID)
            Assert.assertEquals(cursor.getLongFromColumn("task_due_date"), FIELD_DUE_DATE)

            Assert.assertNull(cursor.getStringFromColumn("task_creation_date"))
            Assert.assertNull(cursor.getStringFromColumn("task_completed_date"))
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

        private const val FIELD_ID = 15
        private const val FIELD_IS_COMPLETED = 1
        private const val FIELD_TITLE = "Testing the migration"
        private const val FIELD_DESC = "Testing the migration with all the fields"
        private const val FIELD_CATEGORY_ID = 14
        private const val FIELD_DUE_DATE = 1_568_209_628_447
    }
}
