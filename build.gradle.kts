import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean") {
    delete(rootProject.buildDir)
}

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions {
        allWarningsAsErrors = false
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.Experimental")
        jvmTarget = "1.8"
    }
}
