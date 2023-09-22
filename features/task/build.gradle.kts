import extension.commonDependencies
import extension.commonTestDependencies
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
    alias(libs.plugins.compose)
}

kotlin {
    setFrameworkBaseName("task")

    commonDependencies {
        api(projects.features.categoryApi)
        implementation(projects.features.alarmApi)
        implementation(projects.domain)
        implementation(projects.resources)
        implementation(projects.libraries.di)
        implementation(projects.libraries.navigation)
        implementation(projects.libraries.coroutines)
        implementation(projects.libraries.designsystem)

        implementation(compose.runtime)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)

        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.collections.immutable)
        implementation(libs.koin.compose.jb)
        implementation(libs.moko.resources.compose)
        implementation(libs.moko.mvvm.compose)
    }

    commonTestDependencies {
        implementation(kotlin("test"))
        implementation(projects.libraries.test)
        implementation(libs.kotlinx.datetime)
    }
}
android {
    namespace = "com.escodro.task"
}
