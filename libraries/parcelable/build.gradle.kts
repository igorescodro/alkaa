import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("parcelable")
}

android {
    namespace = "com.escodro.parcelable"
}
