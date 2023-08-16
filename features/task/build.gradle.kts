plugins {
    id("com.escodro.android-compose")
    id("kotlin-parcelize")
}

dependencies {
    api(projects.features.categoryApi)
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.libraries.coroutines)
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)
    implementation(libs.kotlinx.datetime)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)

    runtimeOnly(libs.androidx.playcore)
    implementation(libs.compose.activity)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.accompanist.permission)
}
android {
    namespace = "com.escodro.task"
}
