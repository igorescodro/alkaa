import extension.commonDependencies
import extension.setFrameworkBaseName
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
}

kotlin {
    setFrameworkBaseName("categoryapi")

    commonDependencies {
        implementation(projects.libraries.parcelable)
        api(libs.androidx.lifecycle.viewmodel)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.mvvm.core)
        implementation(libs.moko.parcelize)
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
    namespace = "com.escodro.categoryapi"
}
