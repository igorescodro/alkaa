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
