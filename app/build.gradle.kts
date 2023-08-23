import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.util.Properties

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
        versionCode = Integer.parseInt(libs.versions.version.code.get())
        versionName = libs.versions.version.name.get()

        compileSdk = Integer.parseInt(libs.versions.android.sdk.compile.get())
        minSdk = Integer.parseInt(libs.versions.android.sdk.min.get())
        targetSdk = Integer.parseInt(libs.versions.android.sdk.target.get())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "${parent?.name}-$versionName")
    }

    val properties = readProperties(file("../config/signing/signing.properties"))
    signingConfigs {
        create("release") {
            keyAlias = getSigningKey(properties, "ALKAA_KEY_ALIAS", "keyAlias")
            keyPassword = getSigningKey(properties, "ALKAA_KEY_PASSWORD", "keyPassword")
            storeFile = file(getSigningKey(properties, "ALKAA_STORE_PATH", "storePath"))
            storePassword = getSigningKey(properties, "ALKAA_KEY_STORE_PASSWORD", "storePassword")
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
        htmlOutput = file("${layout.buildDirectory}/reports/lint.html")

        project.tasks.check.dependsOn("lint")
    }

    setDynamicFeatures(setOf(":features:tracker"))

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
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
    namespace = "com.escodro.alkaa"
}

dependencies {
    implementation(projects.libraries.core)
    implementation(projects.libraries.splitInstall)
    implementation(projects.libraries.designsystem)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.coroutines)
    implementation(projects.data.local)
    implementation(projects.data.datastore)
    implementation(projects.data.repository)
    implementation(projects.features.task)
    implementation(projects.features.alarm)
    implementation(projects.features.category)
    implementation(projects.features.preference)
    implementation(projects.features.search)
    implementation(projects.features.glance)

    implementation(projects.domain)
    implementation(projects.shared)

    implementation(platform(libs.compose.bom))

    implementation(libs.logcat)
    implementation(libs.compose.navigation)
    implementation(libs.compose.activity)
    implementation(libs.accompanist.material)
    implementation(libs.androidx.playcore)
    implementation(libs.koin.android)

    implementation(libs.compose.windowsize)

    implementation(libs.bundles.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)

    androidTestUtil(libs.test.orchestrator)

    testImplementation(libs.koin.test)
    testImplementation(libs.test.mockk)

    androidTestImplementation(projects.libraries.test)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.bundles.composetest) {
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.customview", module = "customview")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime")
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

fun getSigningKey(properties: Properties, secretKey: String, propertyKey: String): String =
    if (!System.getenv(secretKey).isNullOrEmpty()) {
        System.getenv(secretKey)
    } else {
        properties.getProperty(propertyKey)
    }
