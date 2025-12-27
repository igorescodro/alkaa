import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("search")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.libraries.designsystem)
            implementation(projects.resources)
            implementation(projects.features.navigationApi)
            implementation(projects.features.taskApi)

            implementation(libs.compose.runtime)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.components.resources)

            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.libraries.test)
            implementation(libs.kotlinx.datetime)
        }
    }

    androidLibrary {
        namespace = "com.escodro.search"
    }
}
