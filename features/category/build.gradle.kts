import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.escodro.kotlin.parcelable)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("category")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.categoryApi)
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.designsystem)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.parcelable)

            implementation(libs.compose.runtime)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.components.resources)

            implementation(libs.koin.compose)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.libraries.test)
        }
    }

    androidLibrary {
        namespace = "com.escodro.category"
    }
}
