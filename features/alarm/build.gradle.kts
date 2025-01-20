import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("search")

    sourceSets {
        commonMain.dependencies {

            implementation(projects.features.alarmApi)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.navigation)
            implementation(projects.libraries.navigationApi)
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(libs.kotlinx.datetime)

            implementation(compose.runtime)
            implementation(compose.components.resources)

            implementation(libs.logging)
            implementation(libs.logback)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.logcat)
            implementation(libs.androidx.core)
        }

        androidUnitTest.dependencies {
            implementation(libs.test.junit)
        }
    }
}

android {
    namespace = "com.escodro.alarm"
}
