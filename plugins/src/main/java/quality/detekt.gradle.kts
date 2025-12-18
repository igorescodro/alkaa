package quality

import dev.detekt.gradle.Detekt
import extension.composeRulesDetekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private val _libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

apply<DetektPlugin>()

dependencies {
    "detektPlugins"(_libs.composeRulesDetekt)
}

configure<DetektExtension> {
    config.setFrom("$rootDir/config/filters/detekt.yml")
    allRules = true
}

// Configure source files for Kotlin Multiplatform projects
pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
    afterEvaluate {
        val kotlin = extensions.findByType<KotlinMultiplatformExtension>()
        if (kotlin != null) {
            tasks.withType<Detekt>().configureEach {
                setSource(kotlin.sourceSets.flatMap { it.kotlin.sourceDirectories })
                exclude("**/resources/**,**/build/**")
            }
        }
    }
}

// Fallback for non-multiplatform projects
tasks.withType<Detekt>().configureEach {
    exclude("**/resources/**,**/build/**")
}
