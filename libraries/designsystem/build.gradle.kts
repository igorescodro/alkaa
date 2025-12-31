import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("designsystem")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.resources)

            implementation(libs.koin.compose)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.runtime)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.collections.immutable)
        }
    }

    androidLibrary {
        namespace = "com.escodro.designsystem"
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.uiTooling)
}
