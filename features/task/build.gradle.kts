import com.android.build.api.dsl.androidLibrary
import extension.setFrameworkBaseName

plugins {
    id("com.escodro.multiplatform")
    id("com.escodro.kotlin-parcelable")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("task")

    sourceSets {
        commonMain.dependencies {
            api(projects.features.categoryApi)
            implementation(projects.features.alarmApi)
            implementation(projects.features.taskApi)
            implementation(projects.domain)
            implementation(projects.resources)
            implementation(projects.features.navigationApi)
            implementation(projects.libraries.coroutines)
            implementation(projects.libraries.designsystem)
            implementation(projects.libraries.parcelable)
            implementation(projects.libraries.permission)

            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.libraries.test)
            implementation(libs.kotlinx.datetime)
        }
    }

    androidLibrary {
        namespace = "com.escodro.task"
    }
}

dependencies {
    "androidRuntimeClasspath"(compose.uiTooling)
}
