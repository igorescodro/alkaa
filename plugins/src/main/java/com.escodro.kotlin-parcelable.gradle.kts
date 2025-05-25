plugins {
    id("com.escodro.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    kotlin("plugin.parcelize")
}

kotlin {
    androidTarget {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.escodro.parcelable.CommonParcelize",
            )
        }
    }
}
