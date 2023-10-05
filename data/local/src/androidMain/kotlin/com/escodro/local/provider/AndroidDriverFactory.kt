package com.escodro.local.provider

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.escodro.local.AlkaaDatabase
import com.escodro.local.Category
import com.escodro.resources.MR
import dev.icerock.moko.resources.desc.desc

/**
 * Provides the platform-specific [SqlDriver] to be used in the database.
 */
internal class AndroidDriverFactory(
    private val context: Context,
) : DriverFactory {

    override fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(AlkaaDatabase.Schema, context, databaseName)

    override fun shouldPrepopulateDatabase(databaseName: String): Boolean =
        !context.getDatabasePath(databaseName).exists()

    override fun getPrepopulateData(): List<Category> = listOf(
        Category(
            category_id = 0,
            category_name = MR.strings.category_default_personal.desc().toString(context),
            category_color = context.getString(MR.colors.blue.resourceId),
        ),
        Category(
            category_id = 0,
            category_name = MR.strings.category_default_work.desc().toString(context),
            category_color = context.getString(MR.colors.green.resourceId),
        ),
        Category(
            category_id = 0,
            category_name = MR.strings.category_default_shopping.desc().toString(context),
            category_color = context.getString(MR.colors.orange.resourceId),
        ),
    )
}
