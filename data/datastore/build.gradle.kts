plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
}

dependencies{
    implementation(projects.data.repository)

    implementation(Deps.koin.android)
    implementation(Deps.android.dataStore)
}
