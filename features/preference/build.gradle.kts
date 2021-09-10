plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.COMPOSE)
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.libraries.designsystem)

    testImplementation(projects.libraries.test)
    androidTestImplementation(projects.libraries.test)
}
