import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("permission")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.compose.runtime)
        }
        androidMain.dependencies {
            implementation(libs.moko.permissions.compose)
            implementation(libs.moko.permissions.notifications)
        }
        iosMain.dependencies {
            implementation(libs.moko.permissions.compose)
            implementation(libs.moko.permissions.notifications)
        }
    }

    androidLibrary {
        namespace = "com.escodro.permission"
    }
}
