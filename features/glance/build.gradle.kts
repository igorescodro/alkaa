plugins {
    id("com.escodro.android-compose")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.shared)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.core)

    implementation(libs.koin.core)
    implementation(libs.androidx.glance)

    // Unwanted dependencies to keep all the Glance-related logic on this module
    implementation(libs.androidx.workmanager)
    implementation(libs.kotlinx.serialization)
}
android {
    namespace = "com.escodro.glance"
}
