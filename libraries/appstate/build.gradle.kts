import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("appstate")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.libraries.parcelable)
            implementation(projects.libraries.navigationApi)

            implementation(compose.runtime)
            implementation(libs.compose.navigation)

            api(libs.compose.windowsizeclass)
        }
    }
}

android {
    namespace = "com.escodro.appstate"
}
