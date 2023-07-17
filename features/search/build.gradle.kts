plugins {
    id("com.escodro.android-compose")
}

dependencies {
    implementation(projects.shared)
    implementation(projects.libraries.designsystem)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)
}
android {
    namespace = "com.escodro.search"
}
