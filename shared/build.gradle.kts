import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    iosX64()
    iosSimulatorArm64()

    listOf(
        iosX64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
        jvm("desktop")
    }

    sourceSets {
        val desktopTest by getting

        commonMain.dependencies {
            implementation(projects.data.local)
            implementation(projects.data.datastore)
            implementation(projects.data.repository)
            implementation(projects.domain)
            implementation(projects.features.navigation)
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.designsystem)
            implementation(projects.libraries.appstate)
            implementation(projects.libraries.parcelable)
            implementation(projects.libraries.permission)

            implementation(projects.features.home)
            implementation(projects.features.task)
            implementation(projects.features.alarm)
            implementation(projects.features.category)
            implementation(projects.features.search)
            implementation(projects.features.preference)
            implementation(projects.features.tracker)

            implementation(projects.resources)

            implementation(projects.domain)

            implementation(libs.compose.runtime)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)

            implementation(libs.koin.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
        }

        androidMain.dependencies {
            implementation(projects.features.glance)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.features.task)
            implementation(projects.libraries.test)

            implementation(libs.compose.uiTest)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.datetime)
        }

        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        androidHostTest.dependencies {
            implementation(libs.test.junit4.android)
            implementation(libs.test.manifest)
        }
    }

    androidLibrary {
        namespace = "com.escodro.shared"
    }
}

// Add compile options to link sqlite3 library allowing iOS UI testing
// https://github.com/touchlab/SQLiter/issues/77#issuecomment-1472089931
project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .flatMap { it.binaries }
        .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
}
