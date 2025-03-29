import extension.androidConfig
import extension.proguardConfig
import extension.logcat

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    androidConfig(libs)
    proguardConfig()
    kotlin {
        jvmToolchain(JavaLanguageVersion.of(17).asInt())
    }
}

dependencies {
    implementation(libs.logcat)
}
