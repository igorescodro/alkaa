import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("tracker")

    androidLibrary {
        namespace = "com.escodro.tracker"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.libraries.designsystem)
            implementation(projects.resources)

            implementation(libs.compose.runtime)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.libraries.test)
            implementation(libs.kotlinx.datetime)
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.uiTooling)
}
