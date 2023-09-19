plugins {
    id("com.escodro.android-compose")
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    testImplementation(projects.libraries.test)
    testImplementation(libs.test.junit)
    testImplementation(libs.kotlinx.datetime)
    androidTestImplementation(projects.libraries.test)
}
android {
    namespace = "com.escodro.search"
}
