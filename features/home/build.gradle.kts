import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("home")

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

            implementation(compose.runtime)
            implementation(compose.materialIconsExtended)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.material3AdaptiveNavigationSuite)

            implementation(libs.koin.compose)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }
}

android {
    namespace = "com.escodro.home"
}
