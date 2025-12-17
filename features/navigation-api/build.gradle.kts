import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    setFrameworkBaseName("navigation-api")

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.navigation.ui)
            api(libs.compose.navigation.adaptive)

            implementation(projects.resources)
            implementation(projects.libraries.parcelable)

            implementation(compose.components.resources)
            implementation(libs.kotlinx.serialization)

            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            implementation(libs.kotlinx.serialization)
        }
    }

    androidLibrary {
        namespace = "com.escodro.navigationapi"
    }
}
