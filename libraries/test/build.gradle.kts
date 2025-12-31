import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    configureTargets("test")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(kotlin("test"))
            api(libs.kotlinx.coroutines.test)
            api(libs.compose.uiTest)
        }

        androidMain.dependencies {
            implementation(kotlin("test-junit"))
            implementation(libs.test.junit4.android)
            implementation(libs.test.uiautomator)
            implementation(libs.test.manifest)
            implementation(libs.test.robolectric)
            implementation(libs.test.work)
        }

        desktopMain.dependencies {
            implementation(kotlin("test-junit"))
        }
    }
    androidLibrary {
        namespace = "com.escodro.test"
    }
}
