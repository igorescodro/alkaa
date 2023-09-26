import extension.commonDependencies
import extension.commonTestDependencies

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    id(libs.plugins.moko.multiplatform.resources.get().pluginId) // Use version from classpath
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    commonDependencies {
        implementation(projects.data.local)
        implementation(projects.data.datastore)
        implementation(projects.data.repository)
        implementation(projects.domain)
        implementation(projects.libraries.navigation)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.designsystem)
        implementation(projects.libraries.di)

        implementation(projects.features.home)
        implementation(projects.features.task)
        implementation(projects.features.category)
        implementation(projects.features.search)
        implementation(projects.features.preference)

        implementation(projects.resources)

        implementation(projects.domain)
        implementation(compose.runtime)
        implementation(compose.material)
        implementation(compose.material3)
        implementation(libs.koin.compose.jb)
        implementation(libs.moko.resources.core)
        implementation(libs.moko.mvvm.compose)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
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
    namespace = "com.escodro.shared"
}

multiplatformResources {
    multiplatformResourcesPackage = "com.escodro.alkaa"
}
