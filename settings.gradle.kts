include(":app")
include(":desktop-app")
include(":features:alarm-api")
include(":features:alarm")
include(":features:task")
include(":features:task-api")
include(":features:category-api")
include(":features:category")
include(":features:preference")
include(":features:tracker")
include(":features:search")
include(":features:glance")
include(":features:navigation")
include(":features:navigation-api")
include(":data:repository")
include(":data:local")
include(":data:datastore")
include(":libraries:coroutines")
include(":libraries:test")
include(":libraries:android-test")
include(":libraries:splitInstall")
include(":libraries:designsystem")
include(":libraries:appstate")
include(":libraries:parcelable")
include(":libraries:permission")

include(":domain")
include(":shared")
include(":resources")

include(":features:home")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
        mavenCentral()
        google()
        includeBuild("plugins")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
