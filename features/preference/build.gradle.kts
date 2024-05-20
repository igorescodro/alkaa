import extension.androidDependencies
import extension.commonDependencies
import extension.iosDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("preference")

    commonDependencies {
        implementation(projects.domain)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.designsystem)
        implementation(projects.resources)
        implementation(projects.libraries.navigation)
        implementation(projects.libraries.di)

        implementation(compose.runtime)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)

        implementation(libs.koin.compose.jb)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
        implementation(libs.aboutlibraries.ui)
    }

    androidDependencies {
        implementation(libs.androidx.corektx)
        implementation(projects.libraries.splitInstall)
    }

    iosDependencies {
        implementation(projects.features.tracker)
    }
}
android {
    namespace = "com.escodro.preference"
}
