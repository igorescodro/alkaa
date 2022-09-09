plugins {
    id("com.escodro.android-compose")
    id("kotlin-parcelize")
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.features.categoryApi)
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
}
