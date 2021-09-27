plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.COMPOSE)
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.libraries.designsystem)

    implementation(Deps.android.constraintLayout)
    implementation(Deps.android.playCore)
}
