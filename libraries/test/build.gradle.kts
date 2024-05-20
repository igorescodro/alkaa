import extension.androidDependencies
import extension.commonDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("test")

    commonDependencies {
        implementation(kotlin("test"))
        api(libs.kotlinx.coroutines.test)
    }

    androidDependencies {
        implementation(kotlin("test-junit"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

android {
    namespace = "com.escodro.test"
}
