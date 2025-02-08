import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("search")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.libraries.designsystem)
            implementation(projects.resources)
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.di)

            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.compose.jb)
            implementation(libs.moko.mvvm.compose)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.libraries.test)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "com.escodro.search"
}
