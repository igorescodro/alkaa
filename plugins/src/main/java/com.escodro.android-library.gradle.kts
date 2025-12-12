import extension.androidConfig
import extension.proguardConfig
import extension.logcat

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

private val _libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    androidConfig(_libs)
    proguardConfig()
    kotlin {
        jvmToolchain(JavaLanguageVersion.of(17).asInt())
    }
}

dependencies {
    implementation(_libs.logcat)
}
