plugins {
    id("com.escodro.kotlin-module")
}

dependencies {
    implementation(projects.domain)

    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}
