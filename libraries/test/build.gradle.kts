plugins {
    id("com.escodro.android-library")
}

android {
    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.escodro.test"
}

dependencies {
    api(libs.test.junit)

    api(libs.kotlinx.coroutines.test)

    implementation(libs.bundles.composetest) {
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.customview", module = "customview")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime")
    }

    implementation(libs.test.uiautomator)
    implementation(libs.test.barista)
}
