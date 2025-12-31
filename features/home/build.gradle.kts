import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.escodro.kotlin.parcelable)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("home")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(projects.features.task)
            implementation(projects.features.category)
            implementation(projects.features.search)
            implementation(projects.features.preference)
            implementation(projects.features.navigation)
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.appstate)
            implementation(projects.libraries.parcelable)
            implementation(projects.libraries.designsystem)

            implementation(libs.compose.runtime)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3AdaptiveNavigationSuite)

            implementation(libs.koin.compose)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
        }
    }

    androidLibrary {
        namespace = "com.escodro.home"
    }
}
