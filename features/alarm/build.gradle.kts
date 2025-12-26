import com.android.build.api.dsl.androidLibrary
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
            implementation(projects.features.navigationApi)
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(libs.kotlinx.datetime)

            implementation(compose.runtime)
            implementation(compose.components.resources)

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
