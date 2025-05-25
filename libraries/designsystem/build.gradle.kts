import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("designsystem")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.compose)
            implementation(compose.runtime)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
    }

    androidLibrary {
        namespace = "com.escodro.designsystem"
    }
}
