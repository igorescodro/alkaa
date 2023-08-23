import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.jvm.toolchain.JavaLanguageVersion

val VersionCatalog.composeVersion: String
    get() = findVersion("compose_compiler").get().requiredVersion

object AlkaaVersions {
    const val versionCode = 20300
    const val versionName = "2.3.0"

    const val compileSdk = 34
    const val targetSdk = 33
    const val minSdk = 24
    val javaCompileVersion = JavaVersion.VERSION_17
    val javaLanguageVersion: JavaLanguageVersion = JavaLanguageVersion.of(17)
}
