plugins {
    id("com.escodro.android-compose")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.navigationApi)
    implementation(projects.libraries.di)

    implementation(libs.koin.core)
    implementation(libs.androidx.glance)

    // dependency for glance theming
    implementation(libs.androidx.glance.material3)
    // using the design system for light and dark theme
    implementation(projects.libraries.designsystem)

    // Unwanted dependencies to keep all the Glance-related logic on this module
    implementation(libs.androidx.workmanager)
    implementation(libs.kotlinx.serialization)
}
android {
    namespace = "com.escodro.glance"
}
