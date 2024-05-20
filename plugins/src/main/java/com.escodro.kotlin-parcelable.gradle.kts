import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
}

kotlin {
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
