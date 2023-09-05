package quality

import ktlint

val ktlint: Configuration by configurations.creating
val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    ktlint(libs.ktlint)
}

tasks {
    register<JavaExec>("ktlint") {
        description = "Check Kotlin code style."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        val buildDir = layout.buildDirectory.get().asFile.path
        args(
            "src/**/*.kt", "--reporter=plain", "--reporter=checkstyle," +
                "output=${buildDir}/reports/ktlint.xml"
        )
    }

    register<JavaExec>("ktlintFormat") {
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args("-F", "src/**/*.kt")
    }
}
