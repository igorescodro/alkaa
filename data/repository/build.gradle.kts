plugins {
    id(GradlePlugin.KOTLIN_LIBRARY)
}

dependencies {
    implementation(projects.domain)

    implementation(Deps.koin.core)
    implementation(Deps.coroutines.core)
}
