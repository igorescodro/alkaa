import extension.setFrameworkBaseName
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.escodro.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    setFrameworkBaseName("desktop-app")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.resources)
            implementation(projects.libraries.appstate)

            implementation(libs.compose.runtime)
            implementation(libs.compose.components.resources)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    androidLibrary {
        namespace = "desktop"
    }
}

compose.desktop {
    application {
        mainClass = "com.escodro.desktopapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.escodro.alkaa"
            packageVersion = libs.versions.version.name.get()
            modules("java.sql")

            macOS {
                iconFile.set(project.file("src/desktopMain/resources/ic_launcher.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/ic_launcher.ico"))
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/ic_launcher.png"))
            }
        }

        jvmArgs(
            "-DpackageVersion=${libs.versions.version.name.get()}"
        )
    }
}
