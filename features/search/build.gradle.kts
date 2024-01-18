import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
}

kotlin {
    setFrameworkBaseName("search")

    commonDependencies {
        implementation(projects.domain)
        implementation(projects.libraries.designsystem)
        implementation(projects.resources)
        implementation(projects.libraries.navigation)
        implementation(projects.libraries.di)

        implementation(compose.runtime)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)

        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.koin.compose.jb)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
    }

    commonTestDependencies {
        implementation(kotlin("test"))
        implementation(projects.libraries.test)
        implementation(libs.kotlinx.datetime)
    }
}

android {
    namespace = "com.escodro.search"
}
