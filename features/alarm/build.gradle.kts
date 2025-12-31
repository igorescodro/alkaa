import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("search")

    sourceSets {
        commonMain.dependencies {

            implementation(projects.features.alarmApi)
            implementation(projects.libraries.coroutines)
            implementation(projects.features.navigationApi)
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(libs.kotlinx.datetime)

            implementation(libs.compose.runtime)
            implementation(libs.compose.components.resources)

            implementation(libs.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.logcat)
            implementation(libs.androidx.core)
        }

        androidHostTest.dependencies {
            implementation(libs.test.junit)
        }
    }

    androidLibrary {
        namespace = "com.escodro.alarm"
    }
}
