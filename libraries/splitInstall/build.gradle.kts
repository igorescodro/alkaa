plugins {
    id("com.escodro.android-compose")
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.libraries.designsystem)

    implementation(libs.androidx.playcore)
}
