import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.escodro.kotlin.parcelable)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    configureTargets("navigation-api")

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.navigation.ui)
            api(libs.compose.navigation.adaptive)

            implementation(projects.resources)
            implementation(projects.libraries.parcelable)

            implementation(libs.compose.components.resources)
            implementation(libs.kotlinx.serialization)

            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.materialIconsExtended)

            implementation(libs.kotlinx.serialization)
        }
    }

    androidLibrary {
        namespace = "com.escodro.navigationapi"
    }
}
