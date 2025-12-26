plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "com.escodro.glance"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
        }
        androidMain.dependencies {
            implementation(projects.domain)
            implementation(projects.features.navigationApi)

            implementation(libs.koin.core)
            implementation(libs.androidx.glance)
            implementation(libs.kotlinx.collections.immutable)

            implementation(libs.androidx.glance.material3)
            implementation(projects.libraries.designsystem)

            // Unwanted dependencies to keep all the Glance-related logic on this module
            implementation(libs.androidx.workmanager)
            implementation(libs.kotlinx.serialization)
        }
    }
}
