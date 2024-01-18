import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("alarmapi")
}

android {
    namespace = "com.escodro.alarmapi"
}
