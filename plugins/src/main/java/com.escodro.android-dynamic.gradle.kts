import extension.sdkCompile
import extension.sdkMin
import extension.androidDependencies

plugins {
    kotlin("multiplatform")
    id("com.android.dynamic-feature")
    id("com.escodro.kotlin-quality")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    androidDependencies {
        implementation(project(":app"))
        implementation(libs.playcore)
    }
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    compileSdk = Integer.parseInt(libs.sdkCompile)
    defaultConfig {
        minSdk = Integer.parseInt(libs.sdkMin)
    }
}
