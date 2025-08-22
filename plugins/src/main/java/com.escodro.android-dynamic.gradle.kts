import extension.sdkCompile
import extension.sdkMin
import extension.playcore
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("multiplatform")
    id("com.android.dynamic-feature")
    id("com.escodro.kotlin-quality")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilerOptions {
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(project(":app"))
            implementation(libs.playcore)
        }
    }
}

android {
    compileSdk = Integer.parseInt(libs.sdkCompile)
    defaultConfig {
        minSdk = Integer.parseInt(libs.sdkMin)
    }
}

// Workaround for Gradle 9 validation: ensure Android unit test lint tasks depend on Compose
// resources accessor generation for Android Unit Test variants. This avoids implicit
// dependencies when lint analyzes sources that reference generated accessors.
// See: https://docs.gradle.org/9.0.0/userguide/validation_problems.html#implicit_dependency
val resourceAccessors = tasks.matching { it.name.startsWith("generateResourceAccessorsForAndroidUnitTest") }

// Lint analysis tasks for unit tests (e.g., lintAnalyzeDebugUnitTest)
tasks.matching { it.name.startsWith("lintAnalyze") && it.name.endsWith("UnitTest") }
    .configureEach {
        dependsOn(resourceAccessors)
    }

// Lint model generation tasks for unit tests (e.g., generateDebugUnitTestLintModel)
tasks.matching { it.name.startsWith("generate") && it.name.endsWith("UnitTestLintModel") }
    .configureEach {
        dependsOn(resourceAccessors)
    }
