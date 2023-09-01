plugins {
    id("com.escodro.android-library")
}

dependencies {
    implementation(libs.androidx.corektx)
    api(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)
}
android {
    namespace = "com.escodro.core"
}
