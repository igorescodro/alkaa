import extension.setFrameworkBaseName

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    setFrameworkBaseName("coroutines")

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
