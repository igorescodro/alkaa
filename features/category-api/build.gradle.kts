plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.PARCELIZE)
}

dependencies {
    implementation(Deps.coroutines.core)
    implementation(Deps.koin.android)
}
