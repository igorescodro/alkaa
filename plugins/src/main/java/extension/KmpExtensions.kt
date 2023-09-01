package extension

import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

/**
 * Extension function to configure the common main set of dependencies.
 */
fun KotlinMultiplatformExtension.commonDependencies(
    configure: KotlinDependencyHandler.() -> Unit,
) {
    sourceSets["commonMain"].dependencies { configure() }
}

/**
 * Extension function to configure the common test set of dependencies.
 */
fun KotlinMultiplatformExtension.commonTestDependencies(
    configure: KotlinDependencyHandler.() -> Unit,
) {
    sourceSets["commonTest"].dependencies { configure() }
}

/**
 * Extension function to configure the Android main set of dependencies.
 */
fun KotlinMultiplatformExtension.androidDependencies(
    configure: KotlinDependencyHandler.() -> Unit,
) {
    sourceSets["androidMain"].dependencies { configure() }
}

/**
 * Extension function to configure the iOS main set of dependencies.
 */
fun KotlinMultiplatformExtension.iosDependencies(
    configure: KotlinDependencyHandler.() -> Unit,
) {
    sourceSets["iosMain"].dependencies { configure() }
}

/**
 * Sets the base name for the Objective-C framework.
 *
 * @param name the base name to be set
 */
fun KotlinMultiplatformExtension.setFrameworkBaseName(name: String) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = name
        }
    }
}
