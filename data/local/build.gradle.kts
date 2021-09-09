plugins {
    id(GradlePlugin.ANDROID_LIBRARY)
    id(GradlePlugin.KAPT)
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
                arguments["room.incremental"] = "true"
            }
        }
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.data.repository)

    implementation(Deps.koin.android)
    implementation(Deps.android.room.runtime)
    implementation(Deps.android.room.ktx)
    kapt(Deps.android.room.compiler)

    androidTestImplementation(Deps.test.runner)
    androidTestImplementation(Deps.test.room)

    testImplementation(Deps.test.junit)
    testImplementation(Deps.test.mockk)
    testImplementation(Deps.coroutines.test)
}
