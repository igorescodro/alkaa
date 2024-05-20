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
    setFrameworkBaseName("home")

    commonDependencies {
        implementation(projects.domain)
        implementation(projects.resources)
        implementation(projects.features.task)
        implementation(projects.features.category)
        implementation(projects.features.search)
        implementation(projects.features.preference)
        implementation(projects.libraries.navigation)
        implementation(projects.libraries.appstate)
        implementation(projects.libraries.parcelable)

        implementation(compose.runtime)
        implementation(compose.materialIconsExtended)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(libs.koin.compose.jb)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
    }
}

android {
    namespace = "com.escodro.home"
}
