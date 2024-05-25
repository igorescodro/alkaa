import extension.androidDependencies
import extension.androidTestDependencies
import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("search")

    commonDependencies {
        implementation(projects.features.alarmApi)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.navigation)
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

    androidDependencies {
        implementation(libs.logcat)
        implementation(libs.androidx.core)
    }

    androidTestDependencies {
        implementation(libs.test.junit)
    }
}

android {
    namespace = "com.escodro.alarm"
}
