plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.COMPOSE)
}

dependencies {
    implementation(projects.domain)

    implementation(Deps.koin.android)
    implementation(Deps.koin.compose)
    implementation(Deps.android.glance)
}
