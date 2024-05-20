import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("designsystem")

    commonDependencies {
        implementation(libs.koin.compose.jb)
        implementation(compose.runtime)
        implementation(compose.material)
        implementation(compose.material3)
    }
}

android {
    namespace = "com.escodro.designsystem"
}
