import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("datastore")

    commonDependencies {
        implementation(projects.data.repository)

        implementation(libs.koin.core)
        implementation(libs.androidx.datastore)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
    }
}

android {
    namespace = "com.escodro.datastore"
}
