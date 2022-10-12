plugins {
    id("com.escodro.android-compose")
    id("kotlin-parcelize")
}

dependencies {
    implementation(projects.features.categoryApi)
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)

    implementation(libs.androidx.playcore)
    implementation(libs.compose.activity)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.accompanist.permission)

    testImplementation(libs.test.corektx)
    androidTestImplementation(libs.test.barista)
}
