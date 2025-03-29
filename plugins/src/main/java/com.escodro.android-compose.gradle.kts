import extension.androidConfig
import extension.composeBom
import extension.logcat
import extension.composeBundle
import extension.composeConfig
import extension.kotlinxCollectionsImmutable
import extension.proguardConfig

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

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}

dependencies {
    implementation(libs.logcat)
    implementation(libs.composeBundle)
    implementation(platform(libs.composeBom))
    implementation(libs.kotlinxCollectionsImmutable)
}
