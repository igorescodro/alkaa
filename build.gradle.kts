plugins {
    alias(libs.plugins.android.kmp.plugin) apply false
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
