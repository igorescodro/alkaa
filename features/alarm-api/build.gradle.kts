import extension.setFrameworkBaseName

plugins {
    alias(libs.plugins.escodro.multiplatform)
}

kotlin {
    setFrameworkBaseName("alarmapi")

    androidLibrary {
        namespace = "com.escodro.alarmapi"
    }
}
