package com.escodro.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * SQLite Database migration from V1 to V2. This migration includes new fields on Task table.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Task ADD COLUMN task_creation_date INTEGER")
        database.execSQL("ALTER TABLE Task ADD COLUMN task_completed_date INTEGER")
    }
}

/**
 * SQLite Database migration from V2 to V3. This migration includes new fields on Task table.
 */
@Suppress("MagicNumber")
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Task ADD COLUMN task_is_repeating INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE Task ADD COLUMN task_alarm_interval INTEGER")
    }
}

/**
 * SQLite Database migration from V3 to V4. This migration removes the null fields from Category.
 */
@Suppress("MagicNumber")
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Category RENAME TO Category_temp")
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Category " +
                "(`category_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`category_name` TEXT NOT NULL, " +
                "`category_color` TEXT NOT NULL)"
        )
        database.execSQL(
            "INSERT INTO Category(category_id, category_name, category_color) " +
                "SELECT category_id, category_name, category_color " +
                "FROM Category_temp"
        )
        database.execSQL("DROP TABLE Category_temp;")
    }
}
