import extension.androidConfig
import extension.composeBom
import extension.composeBundle
import extension.composeConfig
import extension.kotlinxCollectionsImmutable
import extension.logcat
import extension.proguardConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

private val _libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    androidConfig(_libs)
    proguardConfig()
    composeConfig()

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.addAll("-Xstring-concat=inline")
        }
    }
}

dependencies {
    implementation(_libs.logcat)
    implementation(_libs.composeBundle)
    implementation(platform(_libs.composeBom))
    implementation(_libs.kotlinxCollectionsImmutable)
}
