import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    configureTargets("search")

    sourceSets {
        val desktopTest by getting

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
            implementation(libs.compose.uiTest)
        }

        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    androidLibrary {
        namespace = "com.escodro.search"
    }
}
