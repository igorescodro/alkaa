plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "com.escodro.splitinstall"
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(projects.libraries.designsystem)

            implementation(libs.androidx.playcore)
            implementation(libs.androidx.annotation)

            implementation(libs.logcat)
        }
    }
}
