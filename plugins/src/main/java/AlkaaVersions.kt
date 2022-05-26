import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.jvm.toolchain.JavaLanguageVersion

val VersionCatalog.composeVersion: String get() = findVersion("compose").get().requiredVersion

object AlkaaVersions {
    const val versionCode = 20102
    const val versionName = "2.1.2"

    const val compileSdk = 31
    const val targetSdk = 31
    const val minSdk = 24
    val javaCompileVersion = JavaVersion.VERSION_11
    val javaLanguageVersion: JavaLanguageVersion = JavaLanguageVersion.of(11)
}
