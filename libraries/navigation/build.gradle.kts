import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
    alias(libs.plugins.compose)
}

kotlin {
    setFrameworkBaseName("navigation")

    commonDependencies {
        implementation(compose.runtime)
        implementation(compose.material)

        api(libs.voyager.navigator)
        api(libs.voyager.bottomsheet)

        implementation(libs.moko.parcelize)
    }
}

android {
    namespace = "com.escodro.navigation"
}
