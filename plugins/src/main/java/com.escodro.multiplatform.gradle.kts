import extension.sdkCompile
import extension.sdkMin

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.escodro.kotlin-quality")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    compileSdk = Integer.parseInt(libs.sdkCompile)
    defaultConfig {
        minSdk = Integer.parseInt(libs.sdkMin)
    }

    // https://github.com/Kotlin/kotlinx-atomicfu/pull/344
    packaging {
        resources.excludes.apply {
            add("META-INF/versions/9/previous-compilation-data.bin")
        }
    }
}

