plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.COMPOSE)
}

dependencies {
    implementation(Deps.koin.android)
    implementation(Deps.compose.viewModel)
    implementation(Deps.android.windowManager)
}
