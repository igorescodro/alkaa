import extension.commonDependencies
import extension.setFrameworkBaseName
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("appstate")

    commonDependencies {
        implementation(projects.libraries.parcelable)

        implementation(compose.runtime)

        api(libs.compose.windowsizeclass)
    }
}

android {
    namespace = "com.escodro.appstate"
}
