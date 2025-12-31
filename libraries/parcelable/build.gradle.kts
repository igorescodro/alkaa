import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    id("kotlin-parcelize")
}

kotlin {
    configureTargets("parcelable")

    androidLibrary {
        namespace = "com.escodro.parcelable"
    }
}
