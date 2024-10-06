import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setFrameworkBaseName("navigation")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.libraries.parcelable)
            implementation(projects.libraries.coroutines)

            implementation(compose.runtime)
            implementation(compose.material)

            implementation(libs.koin.compose.jb)

            api(libs.voyager.navigator)
            api(libs.voyager.bottomsheet)

            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization)
        }
    }
}

android {
    namespace = "com.escodro.navigation"
}
