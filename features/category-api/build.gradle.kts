import extension.setFrameworkBaseName

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.escodro.kotlin.parcelable)
}

kotlin {
    setFrameworkBaseName("categoryapi")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.libraries.parcelable)
            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.collections.immutable)
        }
    }

    androidLibrary {
        namespace = "com.escodro.categoryapi"
    }
}
