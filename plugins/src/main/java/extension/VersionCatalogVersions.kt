package extension

import org.gradle.api.artifacts.VersionCatalog

val VersionCatalog.composeVersion: String
    get() = findVersion("compose_compiler").get().requiredVersion

val VersionCatalog.sdkCompile: String
    get() = findVersion("android.sdk.compile").get().requiredVersion

val VersionCatalog.sdkMin: String
    get() = findVersion("android.sdk.min").get().requiredVersion
