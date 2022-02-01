object Releases {
    const val versionCode = 20101
    const val versionName = "2.1.1"
}

object Versions {
    const val compileSdk = 31
    const val targetSdk = 31
    const val minSdk = 24

    const val material = "1.3.0"
    const val constraint = "2.0.4"
    const val ktx = "1.0.2"
    const val room = "2.4.1"
    const val playCore = "1.10.0"
    const val dataStore = "1.0.0"
    const val glance = "1.0.0-alpha01"

    const val coroutines = "1.4.0"

    const val logging = "1.12.5"
    const val logback = "1.2.6"
    const val logcat = "0.1"

    const val koin = "3.1.0"

    const val testJunit = "4.12"
    const val testRunner = "1.1.1"
    const val testCore = "1.4.0"
    const val testMockk = "1.12.0"
    const val testUiAutomator = "2.2.0"
    const val testJunitExt = "1.1.0"
    const val testRoom = "2.1.0"
    const val barista = "4.0.0"

    const val compose = "1.1.0-rc03"
    const val composeNav = "2.4.0"
    const val composeVm = "2.4.0"
    const val composeActivity = "1.3.0"

    const val accompanist = "0.22.1-rc"

    const val ktlint = "0.43.2"
}

object Deps {
    const val logging = "io.github.microutils:kotlin-logging:${Versions.logging}"
    const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"
    const val logcat = "com.squareup.logcat:logcat:${Versions.logcat}"
    val android = AndroidDeps
    val coroutines = CoroutinesDeps
    val koin = KoinDeps
    val compose = ComposeDeps
    val accompanist = AccompanistDeps
    val test = TestDeps
    val quality = QualityDeps
}

object AndroidDeps {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
    const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
    const val glance = "androidx.glance:glance-appwidget:${Versions.glance}"
    val room = RoomDeps
}

object CoroutinesDeps {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

object RoomDeps {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
}

object KoinDeps {
    const val core = "io.insert-koin:koin-core:${Versions.koin}"
    const val android = "io.insert-koin:koin-android:${Versions.koin}"
    const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    const val test = "io.insert-koin:koin-test:${Versions.koin}"
}

object ComposeDeps {
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val icons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.composeNav}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeVm}"
    const val activity = "androidx.activity:activity-compose:${Versions.composeActivity}"
    const val uiTest = "androidx.compose.ui:ui-test:${Versions.compose}"
    const val junit4 = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    const val manifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
}

object AccompanistDeps {
    const val animation =
        "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"
}

object TestDeps {
    const val junit = "junit:junit:${Versions.testJunit}"
    const val runner = "androidx.test:runner:${Versions.testRunner}"
    const val core = "androidx.test:core:${Versions.testCore}"
    const val coreKtx = "androidx.test:core-ktx:${Versions.testCore}"
    const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.testUiAutomator}"
    const val junitExt = "androidx.test.ext:junit:${Versions.testJunitExt}"
    const val mockk = "io.mockk:mockk:${Versions.testMockk}"
    const val room = "androidx.room:room-testing:${Versions.testRoom}"
    const val barista = "com.adevinta.android:barista:${Versions.barista}"
}

object QualityDeps {
    const val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
}
