plugins {
    id("com.escodro.android-compose")
}

dependencies {
    implementation(projects.libraries.designsystem)

    implementation(libs.androidx.playcore)
}
android {
    namespace = "com.escodro.splitinstall"
}
