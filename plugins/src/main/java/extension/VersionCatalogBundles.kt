package extension

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

internal val VersionCatalog.composeBundle: Provider<ExternalModuleDependencyBundle>
    get() = getBundle("compose")

internal val VersionCatalog.composeTestBundle: Provider<ExternalModuleDependencyBundle>
    get() = getBundle("composetest")

private fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()
