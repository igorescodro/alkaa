plugins {
    id("com.escodro.android-library") // TODO revert to Kotlin-only
}

dependencies {
    implementation(projects.domain)

    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}

android {
    namespace = "com.escodro.repository"
}
