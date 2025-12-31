import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    configureTargets("repository")

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
