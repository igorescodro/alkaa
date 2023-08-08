plugins {
    id("com.escodro.android-library")
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        javaCompileOptions {
            ksp {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
            }
        }
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
    namespace = "com.escodro.local"
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.data.repository)

    implementation(libs.koin.core)
    api(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.test.junitext)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.androidx.room.test)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}
