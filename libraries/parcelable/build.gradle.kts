import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
}

kotlin {
    setFrameworkBaseName("parcelable")
}

android {
    namespace = "com.escodro.parcelable"
}
