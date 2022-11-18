plugins {
    id("com.escodro.android-library")
    id("kotlin-parcelize")
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.collections.immutable)
}
