import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("category")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.categoryApi)
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(projects.libraries.navigationApi)
            implementation(projects.libraries.designsystem)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.di)
            implementation(projects.libraries.test)
            implementation(projects.libraries.parcelable)

            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)

            implementation(libs.koin.compose.jb)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.moko.mvvm.compose)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.escodro.category"
}
