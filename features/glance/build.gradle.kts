plugins {
    id("com.escodro.android-compose")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.navigation)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.androidx.glance)
}
