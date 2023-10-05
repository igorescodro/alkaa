package com.escodro.local.provider

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.escodro.local.AlkaaDatabase
import com.escodro.local.Category
import com.escodro.resources.MR
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.getUIColor
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DoubleVarOf
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import platform.CoreGraphics.CGColorGetComponents
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.UIKit.UIColor

internal class IosDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(AlkaaDatabase.Schema, databaseName)

    override fun shouldPrepopulateDatabase(databaseName: String): Boolean =
        !databaseExists(databaseName)

    override fun getPrepopulateData(): List<Category> = listOf(
        Category(
            category_id = 0,
            category_name = MR.strings.category_default_personal.desc().localized(),
            category_color = MR.colors.blue.getUIColor().toHex(),
        ),
        Category(
            category_id = 0,
            category_name = MR.strings.category_default_work.desc().localized(),
            category_color = MR.colors.green.getUIColor().toHex(),
        ),
        Category(
            category_id = 0,
            category_name = MR.strings.category_default_shopping.desc().localized(),
            category_color = MR.colors.orange.getUIColor().toHex(),
        ),
    )

    private fun databaseExists(databaseName: String): Boolean {
        val fileManager = NSFileManager.defaultManager
        val documentDirectory = NSFileManager.defaultManager.URLsForDirectory(
            NSLibraryDirectory,
            NSUserDomainMask,
        ).last() as NSURL
        val file = documentDirectory
            .URLByAppendingPathComponent("$DATABASE_PATH$databaseName")?.path
        return fileManager.fileExistsAtPath(file ?: "")
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun UIColor.toHex(): String {
        val components: CPointer<DoubleVarOf<CGFloat>>? = CGColorGetComponents(CGColor)
        val r = components?.get(0)?.times(255)?.toInt()?.toString(16)?.padStart(2, '0') ?: "00"
        val g = components?.get(1)?.times(255)?.toInt()?.toString(16)?.padStart(2, '0') ?: "00"
        val b = components?.get(2)?.times(255)?.toInt()?.toString(16)?.padStart(2, '0') ?: "00"
        return "#$r$g$b"
    }

    private companion object {
        private const val DATABASE_PATH = "Application Support/databases/"
    }
}
