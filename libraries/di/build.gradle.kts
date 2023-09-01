import extension.androidDependencies
import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("di")

    commonDependencies {
        implementation(libs.koin.core)
        implementation(libs.moko.mvvm.core)
    }
    androidDependencies {
        implementation(libs.koin.android)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
    }
}

android {
    namespace = "com.escodro.di"
}
