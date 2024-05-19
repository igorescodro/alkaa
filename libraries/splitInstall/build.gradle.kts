plugins {
    id("com.escodro.android-compose")
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(projects.libraries.designsystem)

    implementation(libs.androidx.playcore)
}
android {
    namespace = "com.escodro.splitinstall"
}
