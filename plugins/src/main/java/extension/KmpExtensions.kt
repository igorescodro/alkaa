package extension

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Sets the base name for the Objective-C framework.
 *
 * @param name the base name to be set
 */
fun KotlinMultiplatformExtension.setFrameworkBaseName(name: String) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = name
        }
    }
    jvm("desktop")
}
