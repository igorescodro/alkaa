import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
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
