import extension.configureTargets

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    configureTargets("alarmapi")

    androidLibrary {
        namespace = "com.escodro.alarmapi"
    }
}
