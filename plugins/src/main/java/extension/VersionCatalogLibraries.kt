package extension

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider


internal val VersionCatalog.ktlint: Provider<MinimalExternalModuleDependency>
    get() = getLibrary("ktlint")

internal val VersionCatalog.composeRulesDetekt: Provider<MinimalExternalModuleDependency>
    get() = getLibrary("composerules-detekt")

private fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()
