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

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/INDEX.LIST")
        }
    }
}

dependencies {
    implementation(libs.logcat)
}
