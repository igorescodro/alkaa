import extensions.addComposeConfig
import extensions.addComposeDependencies
import extensions.addDefaultConfig

plugins {
    id(GradlePlugin.DYNAMIC_FEATURE)
}

android {
    addDefaultConfig()
    addComposeConfig()
}

dependencies {
    implementation(projects.domain)
    implementation(projects.libraries.designsystem)

    implementation(Deps.koin.android)
    implementation(Deps.coroutines.core)
    implementation(Deps.koin.compose)
    implementation(Deps.compose.activity)

    addComposeDependencies()

    testImplementation(projects.libraries.test)
}
