package quality

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

apply<DetektPlugin>()

configure<DetektExtension> {
    config = files("$rootDir/config/filters/detekt.yml")
    allRules = true
}

tasks.withType<Detekt>().configureEach {
    exclude("**/resources/**,**/build/**")
}
