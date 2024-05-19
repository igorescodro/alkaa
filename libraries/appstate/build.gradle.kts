import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("appstate")

    commonDependencies {
        implementation(compose.runtime)
        api(libs.compose.windowsizeclass)
    }
}

android {
    namespace = "com.escodro.appstate"
}
