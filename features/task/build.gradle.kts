plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.COMPOSE)
    id(GradlePlugin.PARCELIZE)
}

dependencies {
    implementation(projects.features.categoryApi)
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)

    implementation(Deps.android.playCore)

    implementation(Deps.koin.android)
    implementation(Deps.koin.compose)

    testImplementation(Deps.test.coreKtx)
    androidTestImplementation(Deps.compose.activity)
    androidTestImplementation(Deps.test.barista)
}
