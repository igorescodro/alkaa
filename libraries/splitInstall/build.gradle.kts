plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
}

dependencies {
    implementation(projects.libraries.core)

    implementation(Deps.android.constraintLayout)
    implementation(Deps.android.playCore)
}
