import extension.androidConfig
import extension.logcat
import extension.proguardConfig

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

kotlin {
    androidLibrary {
        androidConfig(libs)
        proguardConfig()
        kotlin {
            jvmToolchain(JavaLanguageVersion.of(17).asInt())
        }
    }
}

dependencies {
    implementation(libs.logcat)
}
