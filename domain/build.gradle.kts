import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("domain")

    commonDependencies {
        implementation(libs.koin.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.logging)
        implementation(libs.logback)
        implementation(libs.kotlinx.datetime)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
        implementation(libs.kotlinx.coroutines.test)
    }
}

android {
    namespace = "com.escodro.domain"
}
