import extension.commonDependencies
import extension.commonTestDependencies

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    kotlin("native.cocoapods")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Alkaa Multiplatform Module"
        homepage = "https://github.com/igorescodro/alkaa"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios-app/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    commonDependencies {
        implementation(projects.data.local)
        implementation(projects.data.datastore)
        implementation(projects.data.repository)
        implementation(projects.domain)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.designsystem)

        implementation(libs.koin.core)
        implementation(projects.domain)
        implementation(compose.runtime)
        implementation(compose.material3)
    }
    commonTestDependencies {
        implementation(kotlin("test"))
    }
}

android {
    namespace = "com.escodro.shared"
}
