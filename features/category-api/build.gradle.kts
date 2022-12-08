plugins {
    id("com.escodro.android-library")
    id("kotlin-parcelize")
}

dependencies {
    api(libs.androidx.lifecycle.viewmodel)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.collections.immutable)
}
