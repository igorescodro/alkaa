object Releases {
    const val versionCode = 20000
    const val versionName = "2.0.0"
}

object Versions {
    const val compileSdk = 31
    const val targetSdk = 31
    const val minSdk = 24

    const val kotlin = "1.5.10"
    const val material = "1.3.0"
    const val constraint = "2.0.4"
    const val ktx = "1.0.2"
    const val room = "2.3.0"
    const val playCore = "1.10.0"

    const val coroutines = "1.4.0"

    const val logging = "2.0.6"

    const val koin = "3.1.0"

    const val testJunit = "4.12"
    const val testRunner = "1.1.1"
    const val testCore = "1.1.0"
    const val testMockk = "1.12.0"
    const val testUiAutomator = "2.2.0"
    const val testJunitExt = "1.1.0"
    const val testRoom = "2.1.0"
    const val barista = "4.0.0"

    const val compose = "1.0.0"
    const val composeNav = "2.4.0-alpha04"
    const val composeVm = "1.0.0-alpha06"
    const val composeActivity = "1.3.0-beta01"

    const val buildGradle = "7.1.0-alpha08"

    const val ktlint = "0.42.1"
}

object Deps {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val logging = "io.github.microutils:kotlin-logging-jvm:${Versions.logging}"
    val android = AndroidDeps
    val coroutines = CoroutinesDeps
    val koin = KoinDeps
    val compose = ComposeDeps
    val test = TestDeps
    val quality = QualityDeps
    val gradle = GradleDeps
}

object AndroidDeps {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val playCore = "com.google.android.play:core:${Versions.playCore}"
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

object GradleDeps {
    const val buildGradle = "com.android.tools.build:gradle:${Versions.buildGradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}
