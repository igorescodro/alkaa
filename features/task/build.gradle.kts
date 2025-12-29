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
        val desktopTest by getting

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

            implementation(libs.compose.runtime)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.koin.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.logging)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.libraries.test)
            implementation(libs.kotlinx.datetime)
            implementation(libs.compose.uiTest)
        }

        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    androidLibrary {
        namespace = "com.escodro.task"
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.uiTooling)
}
