import extension.androidDependencies
import extension.commonDependencies
import extension.commonTestDependencies
import extension.iosDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    setFrameworkBaseName("local")

    commonDependencies {
        implementation(projects.libraries.coroutines)
        implementation(projects.data.repository)
        implementation(projects.resources)

        implementation(libs.koin.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.sqldelight.coroutines)
        implementation(libs.moko.resources.core)
    }
    androidDependencies {
        implementation(libs.sqldelight.driver)
    }
    iosDependencies {
        implementation(libs.sqldelight.native)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
        implementation(libs.kotlinx.coroutines.test)
    }
}

android {
    namespace = "com.escodro.local"
}

sqldelight {
    databases {
        create("AlkaaDatabase") {
            packageName.set("com.escodro.local")
        }
    }
}
