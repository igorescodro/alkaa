package gradle

import Deps
import extensions.addDefaultConfig

plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("gradle.quality")
}

android {
    addDefaultConfig()
}

dependencies {
    implementation(Deps.logging)
    implementation(project(":app"))
    androidTestImplementation(project(":app"))

    implementation(Deps.android.playCore)
}
