plugins {
    id("com.escodro.android-library")
}

dependencies {
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.core)
    implementation(projects.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.android)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
}
