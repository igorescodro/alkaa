import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
}

kotlin {
    setFrameworkBaseName("navigation")

    commonDependencies {
        implementation(compose.runtime)
        implementation(compose.material)
        api(libs.voyager.navigator)
        api(libs.voyager.bottomsheet)

    }
}

android {
    namespace = "com.escodro.navigation"
}
