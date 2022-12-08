plugins {
    id("com.escodro.android-compose")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.core)

    implementation(libs.koin.core)
    implementation(libs.androidx.glance)

    // Unwanted dependencies to keep all the Glance-related logic on this module
    implementation(libs.androidx.workmanager)
    implementation(libs.kotlinx.serialization)
}
