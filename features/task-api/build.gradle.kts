import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("taskapi")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
        }
    }

    androidLibrary {
        namespace = "com.escodro.taskapi"
    }
}
