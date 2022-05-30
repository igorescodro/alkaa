plugins {
    id("com.escodro.android-library")
}

dependencies{
    implementation(projects.data.repository)

    implementation(libs.koin.android)
    implementation(libs.androidx.datastore)
}
