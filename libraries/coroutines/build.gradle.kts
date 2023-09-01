import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("coroutines")

    commonDependencies {
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.koin.core)
    }
}

android {
    namespace = "com.escodro.coroutines"
}
