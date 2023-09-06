import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
}

kotlin {
    setFrameworkBaseName("categoryapi")

    commonDependencies {
        api(libs.androidx.lifecycle.viewmodel)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.parcelize)
    }
}

android {
    namespace = "com.escodro.categoryapi"
}



