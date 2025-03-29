import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("preference")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.designsystem)
            implementation(projects.resources)
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.di)

            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)

            implementation(libs.koin.compose.jb)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.aboutlibraries.ui)
        }

        androidMain.dependencies {
            implementation(libs.androidx.corektx)
            implementation(projects.libraries.splitInstall)
        }

        iosMain.dependencies {
            implementation(projects.features.tracker)
        }

        desktopMain.dependencies {
            implementation(projects.features.tracker)
        }
    }
}
android {
    namespace = "com.escodro.preference"
}
