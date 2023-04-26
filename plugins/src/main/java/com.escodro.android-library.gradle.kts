import extension.androidConfig
import extension.proguardConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    androidConfig()
    proguardConfig()
    kotlin {
        jvmToolchain(AlkaaVersions.javaLanguageVersion.asInt())
    }
}

dependencies {
    implementation(libs.logcat)
}
