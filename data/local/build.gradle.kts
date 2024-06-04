import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("local")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.libraries.coroutines)
            implementation(projects.data.repository)
            implementation(projects.resources)

            implementation(compose.runtime)
            implementation(compose.components.resources)

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
    }
}

android {
    namespace = "com.escodro.local"
}

sqldelight {
    databases {
        create("AlkaaDatabase") {
            packageName.set("com.escodro.local")
        }
    }
}
