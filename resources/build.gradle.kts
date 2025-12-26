import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    setFrameworkBaseName("resources")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.components.resources)
            implementation(libs.koin.core)
        }
    }

    androidLibrary {
        namespace = "com.escodro.resources"
    }
}

    compose.resources {
        publicResClass = true
        packageOfResClass = "com.escodro.resources"
        generateResClass = always
    }
