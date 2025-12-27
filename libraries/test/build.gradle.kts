import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("test")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(kotlin("test"))
            api(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(kotlin("test-junit"))
        }

        desktopMain.dependencies {
            implementation(kotlin("test-junit"))
        }
    }
    androidLibrary {
        namespace = "com.escodro.test"
    }
}
