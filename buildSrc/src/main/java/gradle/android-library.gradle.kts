package gradle

import Deps
import extensions.addDefaultConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("gradle.quality")
}

android {
    addDefaultConfig()

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(Deps.logging)
}
