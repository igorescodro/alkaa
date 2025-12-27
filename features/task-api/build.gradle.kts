import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("taskapi")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
        }
    }

    androidLibrary {
        namespace = "com.escodro.taskapi"
    }
}
