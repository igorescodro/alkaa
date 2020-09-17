object Releases {
    const val versionCode = 10700
    const val versionName = "1.7.0"
}

object Versions {
    const val compileSdk = 29
    const val targetSdk = 29
    const val minSdk = 23

    const val kotlin = "1.4.0"
    const val material = "1.1.0-beta02"
    const val recyclerView = "1.0.0"
    const val preference = "1.0.0"
    const val cardView = "1.0.0"
    const val constraintLayout = "2.0.0-beta4"
    const val ktx = "1.0.2"
    const val lifecycle = "2.2.0"
    const val room = "2.2.3"
    const val navigation = "2.3.0"
    const val playCore = "1.6.1"

    const val coroutines = "1.3.6"

    const val timber = "4.7.1"

    const val koin = "2.2.0-beta-1"

    const val mpAndroidChart = "3.1.0"
    const val groupie = "2.7.0"

    const val testJunit = "4.12"
    const val testRunner = "1.1.1"
    const val testRules = "1.1.1"
    const val testCore = "1.1.0"
    const val testMockk = "1.9.3"
    const val testEspressoCore = "3.1.1"
    const val testEspressoIntents = "3.1.1"
    const val testEspressoContrib = "3.1.0-alpha4"
    const val testUiAutomator = "2.2.0"
    const val testJunitExt = "1.1.0"
    const val testRoom = "2.1.0"
    const val testArch = "2.1.0"

    const val compose = "1.0.0-alpha02"

    const val buildGradle = "4.2.0-alpha10"

    const val detekt = "1.13.1"
    const val ktlint = "0.39.0"
}

object Deps {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:v${Versions.mpAndroidChart}"
    val android = AndroidDeps
    val coroutines = CoroutinesDeps
    val koin = KoinDeps
    val groupie = GroupieDeps
    val compose = ComposeDeps
    val test = TestDeps
    val quality = QualityDeps
    val gradle = GradleDeps
}

object AndroidDeps {
    val material = "com.google.android.material:material:${Versions.material}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val preference = "androidx.preference:preference:${Versions.preference}"
    val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val playCore = "com.google.android.play:core:${Versions.playCore}"
    val room = RoomDeps
    val navigation = NavigationDeps
    val lifecycle = LifecycleDeps
}

object CoroutinesDeps {
    val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

object LifecycleDeps {
    val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
}

object RoomDeps {
    val runtime = "androidx.room:room-runtime:${Versions.room}"
    val compiler = "androidx.room:room-compiler:${Versions.room}"
    val ktx = "androidx.room:room-ktx:${Versions.room}"
}

object NavigationDeps {
    val fragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    val ui = "androidx.navigation:navigation-ui:${Versions.navigation}"
}

object KoinDeps {
    val core = "org.koin:koin-android:${Versions.koin}"
    val viewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val test = "org.koin:koin-test:${Versions.koin}"
}

object GroupieDeps {
    val core = "com.xwray:groupie:${Versions.groupie}"
    val ktx = "com.xwray:groupie-kotlin-android-extensions:${Versions.groupie}"
}

object ComposeDeps {
    val ui = "androidx.compose.ui:ui:${Versions.compose}"
    val material = "androidx.compose.material:material:${Versions.compose}"
    val tooling = "androidx.ui:ui-tooling:${Versions.compose}"
}

object TestDeps {
    val junit = "junit:junit:${Versions.testJunit}"
    val runner = "androidx.test:runner:${Versions.testRunner}"
    val rules = "androidx.test:rules:${Versions.testRules}"
    val core = "androidx.test:core:${Versions.testCore}"
    val uiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.testUiAutomator}"
    val junitExt = "androidx.test.ext:junit:${Versions.testJunitExt}"
    val mockk = "io.mockk:mockk:${Versions.testMockk}"
    val room = "androidx.room:room-testing:${Versions.testRoom}"
    val arch = "androidx.arch.core:core-testing:${Versions.testArch}"
    val lifecycle = "androidx.arch.core:core-testing:${Versions.testArch}"
    val espresso = EspressoDeps
}

object EspressoDeps {
    val core = "androidx.test.espresso:espresso-core:${Versions.testEspressoCore}"
    val intents = "androidx.test.espresso:espresso-intents:${Versions.testEspressoIntents}"
    val contrib = "androidx.test.espresso:espresso-contrib:${Versions.testEspressoContrib}"
}

object QualityDeps {
    val detekt = "io.gitlab.arturbosch.detekt:detekt-cli:${Versions.detekt}"
    val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
}

object GradleDeps {
    val buildGradle = "com.android.tools.build:gradle:${Versions.buildGradle}"
    val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val navigationSafeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}
