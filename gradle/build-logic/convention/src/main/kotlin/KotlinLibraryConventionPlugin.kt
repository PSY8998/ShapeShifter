import app.shapeshifter.configureKotlin
import app.shapeshifter.configureSpotless
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kotlin")
            pluginManager.apply("com.android.lint")

            configureKotlin(enableWarningsAsErrors = true)

            configureSpotless()

            dependencies {
            }
        }
    }
}
