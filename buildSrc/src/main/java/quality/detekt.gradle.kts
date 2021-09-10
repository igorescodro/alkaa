package quality

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

apply<DetektPlugin>()

configure<DetektExtension> {
    config = files("$rootDir/config/filters/detekt.yml")

    reports {
        xml {
            enabled = true
            destination = project.file("build/reports/detekt/report.xml")
        }
        html {
            enabled = true
            destination = project.file("build/reports/detekt/report.html")
        }
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("**/resources/**,**/build/**")
}
