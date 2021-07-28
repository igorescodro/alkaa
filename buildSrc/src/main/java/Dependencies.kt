object Releases {
    const val versionCode = 20000
    const val versionName = "2.0.0"
}

object Versions {
    const val compileSdk = 30
    const val targetSdk = 30
    const val minSdk = 24

    const val kotlin = "1.5.10"
    const val material = "1.3.0"
    const val constraintLayout = "2.0.4"
    const val ktx = "1.0.2"
    const val room = "2.3.0"
    const val playCore = "1.10.0"

    const val coroutines = "1.4.0"

    const val logging = "2.0.6"

    const val koin = "3.1.0"

    const val testJunit = "4.12"
    const val testRunner = "1.1.1"
    const val testCore = "1.1.0"
    const val testMockk = "1.9.3"
    const val testUiAutomator = "2.2.0"
    const val testJunitExt = "1.1.0"
    const val testRoom = "2.1.0"
    const val barista = "3.7.0"

    const val espresso = "3.3.0"

    const val compose = "1.0.0"
    const val composeNav = "2.4.0-alpha01"
    const val composeViewModel = "1.0.0-alpha06"
    const val composeActivity = "1.3.0-beta01"

    const val buildGradle = "7.1.0-alpha05"

    const val detekt = "1.13.1"
    const val ktlint = "0.39.0"
}

object Deps {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val logging = "io.github.microutils:kotlin-logging-jvm:${Versions.logging}"
    val android = AndroidDeps
    val coroutines = CoroutinesDeps
    val koin = KoinDeps
    val compose = ComposeDeps
    val test = TestDeps
    val quality = QualityDeps
    val gradle = GradleDeps
}

object AndroidDeps {
    val material = "com.google.android.material:material:${Versions.material}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val playCore = "com.google.android.play:core:${Versions.playCore}"
    val room = RoomDeps
}

object CoroutinesDeps {
    val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

object RoomDeps {
    val runtime = "androidx.room:room-runtime:${Versions.room}"
    val compiler = "androidx.room:room-compiler:${Versions.room}"
    val ktx = "androidx.room:room-ktx:${Versions.room}"
}

object KoinDeps {
    val core = "io.insert-koin:koin-core:${Versions.koin}"
    val android = "io.insert-koin:koin-android:${Versions.koin}"
    val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    val test = "io.insert-koin:koin-test:${Versions.koin}"
}

object ComposeDeps {
    val ui = "androidx.compose.ui:ui:${Versions.compose}"
    val material = "androidx.compose.material:material:${Versions.compose}"
    val tooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    val icons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    val navigation = "androidx.navigation:navigation-compose:${Versions.composeNav}"
    val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}"
    val activity = "androidx.activity:activity-compose:${Versions.composeActivity}"
    val uiTest = "androidx.compose.ui:ui-test:${Versions.compose}"
    val junit4 = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    val manifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
}

object TestDeps {
    val junit = "junit:junit:${Versions.testJunit}"
    val runner = "androidx.test:runner:${Versions.testRunner}"
    val core = "androidx.test:core:${Versions.testCore}"
    val coreKtx = "androidx.test:core-ktx:${Versions.testCore}"
    val uiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.testUiAutomator}"
    val junitExt = "androidx.test.ext:junit:${Versions.testJunitExt}"
    val mockk = "io.mockk:mockk:${Versions.testMockk}"
    val room = "androidx.room:room-testing:${Versions.testRoom}"
    val barista = "com.schibsted.spain:barista:${Versions.espresso}"
}

object QualityDeps {
    val detekt = "io.gitlab.arturbosch.detekt:detekt-cli:${Versions.detekt}"
    val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
}

object GradleDeps {
    val buildGradle = "com.android.tools.build:gradle:${Versions.buildGradle}"
    val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}
