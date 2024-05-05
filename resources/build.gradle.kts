import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id(libs.plugins.moko.multiplatform.resources.get().pluginId) // Use version from classpath
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    setFrameworkBaseName("resources")

    commonDependencies {
        implementation(libs.moko.resources.core)
        implementation(libs.koin.core)
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
    namespace = "com.escodro.resources"
}

multiplatformResources {
    multiplatformResourcesPackage = "com.escodro.resources"
    iosBaseLocalizationRegion = "en-US"
}

// Define explicit dependency for Moko resources
// https://github.com/icerockdev/moko-resources/issues/421
afterEvaluate {
    tasks.named("iosSimulatorArm64ProcessResources") {
        dependsOn("generateMRcommonMain")
    }
    tasks.named("iosX64ProcessResources") {
        dependsOn("generateMRcommonMain")
    }
}
