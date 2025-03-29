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
