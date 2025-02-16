import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("permission")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            api(libs.moko.permissions.compose)
            api(libs.moko.permissions.notifications)
        }
    }
}

android {
    namespace = "com.escodro.permission"
}
