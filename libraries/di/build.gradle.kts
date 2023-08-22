plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.escodro.kotlin-quality")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "di"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.moko.mvvm.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.escodro.di"
    compileSdk = Integer.parseInt(libs.versions.android.sdk.compile.get())
    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.android.sdk.min.get())
    }
}
