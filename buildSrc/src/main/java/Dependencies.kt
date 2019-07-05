object Releases {
    const val versionCode = 10302
    const val versionName = "1.3.2"
}

object Versions {
    const val compileSdk = 28
    const val targetSdk = 28
    const val minSdk = 23

    const val kotlin = "1.3.31"
    const val appCompat = "1.0.2"
    const val material = "1.0.0"
    const val recyclerView = "1.0.0"
    const val preference = "1.0.0"
    const val cardView = "1.0.0"
    const val androidx = "1.0.0"
    const val constraintLayout = "2.0.0-beta1"
    const val workManager = "1.0.1"
    const val ktx = "1.0.2"
    const val lifecycle = "1.0.0"
    const val room = "2.1.0"
    const val navigation = "1.0.0"

    const val timber = "4.7.1"

    const val koin = "2.0.0"

    const val rxJava = "2.1.0"
    const val rxAndroid = "2.2.0"

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

    const val buildGradle = "3.4.1"
    const val kotlinGradle = "1.3.31"

    const val detekt = "1.0.0-RC14"
    const val ktlint = "0.32.0"
}

object Deps {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val android = AndroidDeps
    val koin = KoinDeps
    val rx = RxDeps
    val test = TestDeps
    val quality = QualityDeps
    val gradle = GradleDeps
}

object AndroidDeps {
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val material = "com.google.android.material:material:${Versions.material}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val preference = "androidx.preference:preference:${Versions.preference}"
    val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val workManager = "android.arch.work:work-runtime:${Versions.workManager}"
    val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    val room = RoomDeps
    val navigation = NavigationDeps
}

object RoomDeps {
    val runtime = "androidx.room:room-runtime:${Versions.room}"
    val compiler = "androidx.room:room-compiler:${Versions.room}"
    val rx = "androidx.room:room-rxjava2:${Versions.room}"
}

object NavigationDeps {
    val fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
    val ui = "android.arch.navigation:navigation-ui:${Versions.navigation}"
}

object KoinDeps {
    val core = "org.koin:koin-android:${Versions.koin}"
    val viewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val test = "org.koin:koin-test:${Versions.koin}"
}

object RxDeps {
    val android = "io.reactivex.rxjava2:rxandroid:${Versions.rxJava}"
    val java = "io.reactivex.rxjava2:rxjava:${Versions.rxAndroid}"
}

object TestDeps {
    val junit = "junit:junit:${Versions.testJunit}"
    val runner = "androidx.test:runner:${Versions.testRunner}"
    val rules = "androidx.test:rules:${Versions.testRules}"
    val core = "androidx.test:core:${Versions.testCore}"
    val uiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.testUiAutomator}"
    val junitExt = "androidx.test.ext:junit:${Versions.testJunitExt}"
    val mockk = "io.mockk:mockk:${Versions.testMockk}"
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
    val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinGradle}"
    val navigationSafeArgs =
        "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}
