import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("datastore")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.repository)

            implementation(libs.koin.core)
            implementation(libs.androidx.datastore)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    androidLibrary {
        namespace = "com.escodro.datastore"
    }
}
