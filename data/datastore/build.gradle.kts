import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    configureTargets("datastore")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.repository)

            implementation(libs.koin.core)
            implementation(libs.androidx.datastore)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    androidLibrary {
        namespace = "com.escodro.datastore"
    }
}
