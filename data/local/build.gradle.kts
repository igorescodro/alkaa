plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.escodro.kotlin-quality")
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
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
            baseName = "local"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.libraries.coroutines)
                implementation(projects.data.repository)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.sqldelight.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "com.escodro.local"
    compileSdk = Integer.parseInt(libs.versions.android.sdk.compile.get())
    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.android.sdk.min.get())
    }
}

sqldelight {
    databases {
        create("AlkaaDatabase") {
            packageName.set("com.escodro.local")
        }
    }
}
