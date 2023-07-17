plugins {
    id("com.escodro.android-compose")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.core)
    implementation(projects.libraries.designsystem)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.aboutlibraries.ui)

    androidTestImplementation(projects.libraries.test)
}
android {
    namespace = "com.escodro.preference"
}
