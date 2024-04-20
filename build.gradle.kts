plugins {
    alias(libs.plugins.dependencyanalysis)
}
buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.aboutlibraries.plugin)
        classpath(libs.kotlin.atomicfu)
        classpath(libs.moko.resources.generator)
    }
}

allprojects {
    apply(plugin = "kotlinx-atomicfu")
    repositories {
        google()
        mavenCentral()
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf(
            "-Xexpect-actual-classes", // Ignore expect/actual experimental state
        )
    }
}
