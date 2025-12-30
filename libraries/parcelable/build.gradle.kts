import extension.setFrameworkBaseName

plugins {
    alias(libs.plugins.escodro.multiplatform)
    id("kotlin-parcelize")
}

kotlin {
    setFrameworkBaseName("parcelable")

    androidLibrary {
        namespace = "com.escodro.parcelable"
    }
}
