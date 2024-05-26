import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
}

kotlin {
    setFrameworkBaseName("test")

    sourceSets {
        commonMain.dependencies {
            implementation(kotlin("test"))
            api(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(kotlin("test-junit"))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

android {
    namespace = "com.escodro.test"
}
