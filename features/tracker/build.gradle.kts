plugins {
    id("com.escodro.android-dynamic")
}

android {
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
    namespace = "com.escodro.tracker"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.compose.activity)

    implementation(libs.bundles.compose)
    implementation(libs.kotlinx.collections.immutable)

    androidTestImplementation(libs.bundles.composetest) {
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.customview", module = "customview")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime")
    }

    testImplementation(projects.libraries.test)
}
