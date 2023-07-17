plugins {
    id("com.escodro.android-library")
}

dependencies {
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.core)
    implementation(projects.shared)

    implementation(libs.androidx.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
}
android {
    namespace = "com.escodro.alarm"
}
