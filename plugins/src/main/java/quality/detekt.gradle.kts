package quality

import dev.detekt.gradle.Detekt
import extension.composeRulesDetekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin

private val _libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

apply<DetektPlugin>()

dependencies {
    "detektPlugins"(_libs.composeRulesDetekt)
}

configure<DetektExtension> {
    config.setFrom("$rootDir/config/filters/detekt.yml")
    allRules = true
}

tasks.withType<Detekt>().configureEach {
    exclude("**/resources/**,**/build/**")
}
