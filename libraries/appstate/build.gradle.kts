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

            implementation(compose.runtime)

            api(libs.compose.windowsizeclass)
        }
    }
}

android {
    namespace = "com.escodro.appstate"
}
