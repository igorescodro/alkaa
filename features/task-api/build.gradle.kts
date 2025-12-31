import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.escodro.kotlin.parcelable)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("taskapi")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
        }
    }

    androidLibrary {
        namespace = "com.escodro.taskapi"
    }
}
