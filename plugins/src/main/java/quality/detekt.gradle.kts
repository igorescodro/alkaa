package quality

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import extension.composeRulesDetekt

private val _libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

apply<DetektPlugin>()

dependencies {
    "detektPlugins"(_libs.composeRulesDetekt)
}

configure<DetektExtension> {
    enableCompilerPlugin = true
    config.setFrom("$rootDir/config/filters/detekt.yml")
    parallel = true
    allRules = true
    buildUponDefaultConfig = true
    debug = false
}

// Configure source files for Kotlin Multiplatform projects
tasks.withType<Detekt>().configureEach {
    exclude("**/resources/**", "**/build/**")

    // Workaround to exclude build and resources folders in KMP projects
    val projectDirFile = layout.projectDirectory.asFile
    exclude {
        it.file.relativeTo(projectDirFile).startsWith("build") ||
                it.file.relativeTo(projectDirFile).startsWith("resources")
    }

    // Ensure detekt runs after resource accessor generation tasks
    mustRunAfter(
        tasks.matching { task ->
            task.name.startsWith("generateResourceAccessorsFor") ||
                    task.name.startsWith("generateActualResourceCollectorsFor") ||
                    task.name.startsWith("generateExpectResourceCollectorsFor") ||
                    task.name.startsWith("generateComposeResClass")
        }
    )
}

tasks.register("detektAll") {
    dependsOn(tasks.withType<Detekt>())
}
