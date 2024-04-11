import app.shapeshifter.configureAndroid
import app.shapeshifter.configureKotlin
import app.shapeshifter.configureSpotless
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            configureAndroid()

            dependencies {
                // Junit Test
                testImplementation(libs.junit)
                androidTestImplementation(libs.test.junit)
                androidTestImplementation(libs.test.runner)
            }
        }
    }
}
