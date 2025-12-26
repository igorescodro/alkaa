import com.android.build.api.dsl.androidLibrary
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
            implementation(compose.runtime)
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
