plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
}

dependencies {
    implementation(projects.features.alarmApi)
    implementation(projects.libraries.core)
    implementation(projects.libraries.navigation)
    implementation(projects.domain)

    implementation(Deps.coroutines.core)
    implementation(Deps.koin.android)

    testImplementation(Deps.test.junit)
    testImplementation(Deps.test.mockk)
}
