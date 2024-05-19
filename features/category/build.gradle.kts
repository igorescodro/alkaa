import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("category")

    commonDependencies {
        implementation(projects.features.categoryApi)
        implementation(projects.domain)
        implementation(projects.resources)
        implementation(projects.libraries.navigation)
        implementation(projects.libraries.designsystem)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.di)
        implementation(projects.libraries.test)
        implementation(projects.libraries.parcelable)

        implementation(compose.runtime)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)

        implementation(libs.koin.compose.jb)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
        implementation(libs.moko.parcelize)
    }

    commonTestDependencies {
        implementation(kotlin("test"))
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.escodro.parcelable.CommonParcelize"
            )
        }
    }
}

android {
    namespace = "com.escodro.category"
}
