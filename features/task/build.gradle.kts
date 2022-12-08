plugins {
    id("com.escodro.android-compose")
    id("kotlin-parcelize")
}

dependencies {
    api(projects.features.categoryApi)
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)

    runtimeOnly(libs.androidx.playcore)
    implementation(libs.compose.activity)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.accompanist.permission)
}
