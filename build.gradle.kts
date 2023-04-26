plugins {
    alias(libs.plugins.dependencyanalysis)
    id("io.github.gmazzo.test.aggregation.coverage") version "1.0.3"
    id("io.github.gmazzo.test.aggregation.results") version "1.0.3"
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
