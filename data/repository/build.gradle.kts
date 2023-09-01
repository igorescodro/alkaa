import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("repository")

    commonDependencies {
        implementation(projects.domain)

        implementation(libs.koin.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.datetime)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
    }
}

android {
    namespace = "com.escodro.repository"
}
