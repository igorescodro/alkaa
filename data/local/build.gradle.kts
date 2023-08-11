plugins {
    id("com.escodro.android-library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
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

    implementation(libs.sqldelight.driver)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.test.junitext)
    androidTestImplementation(libs.test.runner)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

sqldelight {
    databases {
        create("AlkaaDatabase") {
            packageName.set("com.escodro.local")
        }
    }
}
