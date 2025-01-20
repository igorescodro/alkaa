import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setFrameworkBaseName("navigation-api")

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.navigation)
            implementation(projects.resources)
            implementation(projects.libraries.parcelable)

            implementation(compose.components.resources)
            implementation(libs.kotlinx.serialization)

            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            implementation(libs.kotlinx.serialization)
        }
    }
}

android {
    namespace = "com.escodro.navigationapi"
}
