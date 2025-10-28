import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.escodro.kotlin-quality")
    id("com.mikepenz.aboutlibraries.plugin")
    alias(libs.plugins.compose.compiler)
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

        base.archivesName.set("${parent?.name}-$versionName")
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

        lintConfig = file("$rootDir/config/filters/lint.xml")
        htmlOutput = layout.buildDirectory.file("reports/lint.html").get().asFile

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

    packaging {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
            add("META-INF/versions/9/previous-compilation-data.bin")
        }
    }

    namespace = "com.escodro.alkaa"
}

dependencies {
    implementation(projects.shared)
    implementation(projects.features.navigationApi)

    implementation(libs.logcat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.compose.activity)
    implementation(libs.androidx.playcore)
    implementation(libs.koin.core)
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
