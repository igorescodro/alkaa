import extension.commonDependencies
import extension.setFrameworkBaseName
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
}

kotlin {
    setFrameworkBaseName("categoryapi")

    commonDependencies {
        implementation(projects.libraries.parcelable)
        api(libs.androidx.lifecycle.viewmodel)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.mvvm.core)
    }
}

android {
    namespace = "com.escodro.categoryapi"
}
