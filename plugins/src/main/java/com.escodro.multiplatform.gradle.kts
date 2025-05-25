import extension.sdkCompile
import extension.sdkMin
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("com.escodro.kotlin-quality")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilerOptions {
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
        }
    }

    androidLibrary {
        compileSdk = Integer.parseInt(libs.sdkCompile)
        minSdk = Integer.parseInt(libs.sdkMin)
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
