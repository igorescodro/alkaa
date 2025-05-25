import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("alarmapi")

    androidLibrary {
        namespace = "com.escodro.alarmapi"
    }
}
