package com.escodro.local.provider

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.escodro.local.AlkaaDatabase
import me.sujanpoudel.utils.paths.appCacheDirectory

internal class DesktopDriverFactory : DriverFactory {

    override fun createDriver(databaseName: String): SqlDriver {
        val appCacheDirectory = appCacheDirectory(appId = PACKAGE_NAME, createDir = true)
        val jdbcUrl = "jdbc:sqlite:$appCacheDirectory$databaseName"
        return JdbcSqliteDriver(jdbcUrl).use { driver ->
            driver.apply {
                AlkaaDatabase.Schema.create(this)
            }
        }
    }

    private companion object {
        private const val PACKAGE_NAME = "com.escodro.alkaa"
    }
}
