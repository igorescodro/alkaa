import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
    alias(libs.plugins.compose)
}

kotlin {
    setFrameworkBaseName("category")

    commonDependencies {
        implementation(projects.features.categoryApi)
        implementation(projects.domain)
        implementation(projects.resources)
        implementation(projects.libraries.designsystem)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.di)
        implementation(projects.libraries.test)

        implementation(compose.runtime)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)

        implementation(libs.koin.compose.jb)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
    }

    commonTestDependencies {
        implementation(kotlin("test"))
    }
}

android {
    namespace = "com.escodro.category"
}
