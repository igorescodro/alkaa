import extension.androidConfig
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
    kotlin {
        jvmToolchain(JavaLanguageVersion.of(17).asInt())
    }

    // https://github.com/Kotlin/kotlinx-atomicfu/pull/344
    packaging {
        resources.excludes.apply {
            add("META-INF/versions/9/previous-compilation-data.bin")
        }
    }
}

dependencies {
    implementation(libs.logcat)
}
