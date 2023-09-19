import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("test")

    commonDependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
        api(libs.kotlinx.coroutines.test)
    }
}

android {
    namespace = "com.escodro.test"
}
