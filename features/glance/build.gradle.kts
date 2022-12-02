plugins {
    id("com.escodro.android-compose")
    kotlin("plugin.serialization").version(libs.versions.kotlin)
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.core)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.androidx.glance)

    // Unwanted dependencies to keep all the Glance-related logic on this module
    implementation(libs.androidx.workmanager)
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.datastore)
}
