plugins {
    alias(libs.plugins.dependencyanalysis)
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
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
    }
}

allprojects {
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
