plugins {
    id("com.escodro.kotlin-module")
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.logging)
    runtimeOnly(libs.logback)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
