import extension.setFrameworkBaseName

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    setFrameworkBaseName("repository")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    androidLibrary {
        namespace = "com.escodro.repository"
    }
}
