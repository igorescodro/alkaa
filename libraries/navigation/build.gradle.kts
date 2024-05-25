import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.libraries.parcelable)
            implementation(compose.runtime)
            implementation(compose.material)

            api(libs.voyager.navigator)
            api(libs.voyager.bottomsheet)
        }
    }
}

android {
    namespace = "com.escodro.navigation"
}
