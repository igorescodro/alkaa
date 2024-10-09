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
            implementation(projects.libraries.appstate)
            implementation(projects.resources)

            implementation(compose.runtime)
            implementation(compose.material)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            implementation(libs.koin.compose.jb)

            api(libs.voyager.navigator)
            api(libs.voyager.bottomsheet)
            api(libs.compose.navigation)

            implementation(libs.kotlinx.serialization)
        }
    }
}

android {
    namespace = "com.escodro.navigation"
}
