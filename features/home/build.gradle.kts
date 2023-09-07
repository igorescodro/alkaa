import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
}

kotlin {
    setFrameworkBaseName("home")

    commonDependencies {
        implementation(projects.domain)
        implementation(projects.resources)
        implementation(projects.features.task)
        implementation(projects.features.category)

        implementation(compose.runtime)
        implementation(compose.materialIconsExtended)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(libs.koin.compose.jb)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
    }

    // Explicit dependency due to Moko issues with Kotlin 1.9.0
    // https://github.com/icerockdev/moko-resources/issues/531
    sourceSets {
        val commonMain by getting
        val androidMain by getting {
            dependsOn(commonMain)
        }
    }
}

android {
    namespace = "com.escodro.home"
}

