import extension.androidConfig

plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    androidConfig()
}

dependencies {
    implementation(libs.logcat)
    implementation(project(":app"))
    androidTestImplementation(project(":app"))

    implementation(libs.playcore)
}
