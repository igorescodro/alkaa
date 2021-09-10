package extensions

import Versions
import com.android.build.api.dsl.CommonExtension

/**
 * Adds the base Compose configurations on Gradle.
 */
fun CommonExtension<*, *, *, *>.addComposeConfig() {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
}

/**
 * Adds the base default app configurations on Gradle.
 */
fun CommonExtension<*, *, *, *>.addDefaultConfig() {
    defaultConfig {
        compileSdk = Versions.compileSdk
        minSdk = Versions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}


