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
    }

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
}
