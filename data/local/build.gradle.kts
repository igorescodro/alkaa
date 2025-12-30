import extension.setFrameworkBaseName

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("local")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.libraries.coroutines)
            implementation(projects.data.repository)
            implementation(projects.resources)

            implementation(libs.compose.runtime)
            implementation(libs.compose.components.resources)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.sqldelight.coroutines)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.driver)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.native)
            implementation(libs.stately) // Fixes conflict between SQLDelight and Koin
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        desktopMain.dependencies {
            implementation(libs.sqldelight.jvm)
            implementation(libs.multiplatform.path)
        }
    }

    androidLibrary {
        namespace = "com.escodro.local"
    }
}

sqldelight {
    databases {
        create("AlkaaDatabase") {
            packageName.set("com.escodro.local")
        }
    }
}
