import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
    id("kotlin-parcelize")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    defaultConfig {
        applicationId = "com.escodro.alkaa"
        versionCode = AlkaaVersions.versionCode
        versionName = AlkaaVersions.versionName

        compileSdk = AlkaaVersions.compileSdk
        minSdk = AlkaaVersions.minSdk
        targetSdk = AlkaaVersions.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "${parent?.name?.capitalize()}-$versionName")
    }

    signingConfigs {
        create("release") {
            keyAlias = System.getenv("ALKAA_KEY_ALIAS")
            keyPassword = System.getenv("ALKAA_KEY_PASSWORD")
            storeFile = file("../config/signing/alkaa-keystore")
            storePassword = System.getenv("ALKAA_KEY_STORE_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        htmlReport = true
        checkDependencies = true

        lintConfig = file("${rootDir}/config/filters/lint.xml")
        htmlOutput = file("${buildDir}/reports/lint.html")

        project.tasks.check.dependsOn("lint")
    }

    setDynamicFeatures(setOf(":features:tracker"))

    compileOptions {
        sourceCompatibility = AlkaaVersions.javaCompileVersion
        targetCompatibility = AlkaaVersions.javaCompileVersion
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        managedDevices {
            devices {
                add(
                    create<ManagedVirtualDevice>("pixel2api27") {
                        device = "Pixel 2"
                        apiLevel = 27
                        systemImageSource = "aosp"
                    }
                )
            }
            groups {
                create("alkaaDevices").apply {
                    targetDevices.addAll(
                        listOf(devices.getByName("pixel2api27"))
                    )
                }
                unitTests.isReturnDefaultValues = true
            }
        }
    }
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.libraries.splitInstall)
    implementation(projects.libraries.designsystem)
    implementation(projects.libraries.navigation)
    implementation(projects.data.local)
    implementation(projects.data.datastore)
    implementation(projects.data.repository)
    implementation(projects.domain)
    implementation(projects.features.task)
    implementation(projects.features.alarm)
    implementation(projects.features.category)
    implementation(projects.features.preference)
    implementation(projects.features.search)
    implementation(projects.features.glance)

    implementation(libs.logcat)
    implementation(libs.compose.navigation)
    implementation(libs.compose.activity)
    implementation(libs.accompanist.animation)
    implementation(libs.accompanist.material)
    implementation(libs.androidx.playcore)
    implementation(libs.koin.android)

    implementation(libs.bundles.compose)
    implementation(libs.kotlinx.collections.immutable)

    androidTestUtil(libs.test.orchestrator)

    testImplementation(libs.koin.test)
    testImplementation(libs.test.mockk)

    androidTestImplementation(projects.libraries.test)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.bundles.composetest) {
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.customview", module = "customview")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime")
    }
}
