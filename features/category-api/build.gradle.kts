plugins {
    id("com.escodro.android-library")
    id("kotlin-parcelize")
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.collections.immutable)
}
