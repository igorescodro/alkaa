package extension

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

internal val VersionCatalog.androidKmpPlugin: Provider<PluginDependency>
    get() = findPlugin("android-kmp-plugin").get()
