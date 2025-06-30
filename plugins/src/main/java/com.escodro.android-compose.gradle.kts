import extension.androidConfig
import extension.composeBom
import extension.logcat
import extension.composeBundle
import extension.composeConfig
import extension.kotlinxCollectionsImmutable
import extension.proguardConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    androidConfig(libs)
    proguardConfig()
    composeConfig()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll("-Xstring-concat=inline")
    }
}

dependencies {
    implementation(libs.logcat)
    implementation(libs.composeBundle)
    implementation(platform(libs.composeBom))
    implementation(libs.kotlinxCollectionsImmutable)
}
