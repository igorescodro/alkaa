repositories {
    google()
    mavenCentral()
}

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0-alpha06")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")

    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
}
