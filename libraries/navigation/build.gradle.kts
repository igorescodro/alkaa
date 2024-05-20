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
    setFrameworkBaseName("navigation")

    commonDependencies {
        implementation(projects.libraries.parcelable)
        implementation(compose.runtime)
        implementation(compose.material)

        api(libs.voyager.navigator)
        api(libs.voyager.bottomsheet)

        implementation(libs.moko.parcelize)
    }
}

android {
    namespace = "com.escodro.navigation"
}
