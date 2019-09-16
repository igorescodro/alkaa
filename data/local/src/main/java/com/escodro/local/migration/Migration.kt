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
