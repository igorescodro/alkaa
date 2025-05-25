import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
}

kotlin {
    setFrameworkBaseName("parcelable")

    androidLibrary {
        namespace = "com.escodro.parcelable"
    }
}
