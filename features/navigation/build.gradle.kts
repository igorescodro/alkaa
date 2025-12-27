import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setFrameworkBaseName("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.parcelable)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.appstate)
            implementation(projects.libraries.permission)
            implementation(projects.resources)

            implementation(libs.compose.runtime)
            implementation(libs.compose.material)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.materialIconsExtended)

            implementation(libs.koin.compose)

            api(libs.compose.navigation.ui)

            implementation(libs.kotlinx.serialization)
        }
    }

    androidLibrary {
        namespace = "com.escodro.navigation"
    }
}
