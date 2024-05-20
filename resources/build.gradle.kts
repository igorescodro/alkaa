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
}

android {
    namespace = "com.escodro.resources"
}

multiplatformResources {
    resourcesPackage.set("com.escodro.resources")
    iosBaseLocalizationRegion = "en-US"
}
