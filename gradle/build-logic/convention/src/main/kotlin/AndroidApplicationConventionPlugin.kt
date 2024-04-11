import app.shapeshifter.addCoroutinesCompilerArgs
import app.shapeshifter.configureAndroid
import app.shapeshifter.configureKotlin
import app.shapeshifter.configureSpotless
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            configureKotlin(
                enableWarningsAsErrors = true,
                compilerOptions = {
                    addCoroutinesCompilerArgs()
                },
            )

            configureAndroid()

            configureSpotless()

            application {
                buildFeatures {
                    buildConfig = true
                }
            }

            dependencies {
                // Junit Test
                testImplementation(libs.junit)
                androidTestImplementation(libs.test.junit)
                androidTestImplementation(libs.test.runner)
            }
        }
    }
}

private fun Project.application(action: ApplicationExtension.() -> Unit) =
    extensions.configure(action)
