package extensions

import Deps
import org.gradle.api.Action
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.exclude

/**
 * Adds the Compose dependencies on Gradle.
 */
fun DependencyHandler.addComposeDependencies() {
    implementation(Deps.compose.ui)
    implementation(Deps.compose.material)
    implementation(Deps.compose.materialYou)
    implementation(Deps.compose.tooling)
    implementation(Deps.compose.icons)

    androidTestImplementation(Deps.compose.uiTest) {
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.customview", module = "customview")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime")
    }
    androidTestImplementation(Deps.compose.junit4) {
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.customview", module = "customview")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.lifecycle", module = "lifecycle-runtime")
    }

    androidTestImplementation(Deps.compose.manifest)
}

private fun DependencyHandler.implementation(dependencyNotation: String): Dependency? =
    add("implementation", dependencyNotation)

private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

private fun DependencyHandler.androidTestImplementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "androidTestImplementation", dependencyNotation, dependencyConfiguration
)
