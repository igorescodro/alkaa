plugins {
    id("com.escodro.multiplatform")
    id("kotlin-parcelize")
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
