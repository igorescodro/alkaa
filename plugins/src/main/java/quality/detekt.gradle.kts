package quality

import extension.composeRulesDetekt
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

apply<DetektPlugin>()

dependencies {
    "detektPlugins"(libs.composeRulesDetekt)
}

configure<DetektExtension> {
    config.setFrom("$rootDir/config/filters/detekt.yml")
    allRules = true
}

tasks.withType<Detekt>().configureEach {
    exclude("**/resources/**,**/build/**")
}
