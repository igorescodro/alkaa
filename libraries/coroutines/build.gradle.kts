import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    configureTargets("coroutines")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }

    androidLibrary {
        namespace = "com.escodro.coroutines"
    }
}
