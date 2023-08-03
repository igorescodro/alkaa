plugins {
    id("com.escodro.android-library") // TODO revert to Kotlin-only
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.core)

    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}

android {
    namespace = "com.escodro.repository"
}
