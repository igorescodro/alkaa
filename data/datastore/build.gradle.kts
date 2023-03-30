plugins {
    id("com.escodro.android-library")
}

dependencies {
    implementation(projects.data.repository)

    implementation(libs.koin.core)
    implementation(libs.androidx.datastore)
}
android {
    namespace = "com.escodro.datastore"
}
