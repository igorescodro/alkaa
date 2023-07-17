plugins {
    id("com.escodro.kotlin-module")
    id("com.android.lint")
}

dependencies {
    implementation(projects.shared)

    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}
