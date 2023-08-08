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
            baseName = "repository"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.domain)

                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
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
    namespace = "com.escodro.repository"
    compileSdk = Integer.parseInt(libs.versions.android.sdk.compile.get())
    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.android.sdk.min.get())
    }
}
