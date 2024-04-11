import app.shapeshifter.configureKotlin
import app.shapeshifter.configureSpotless
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }

        configureKotlin(enableWarningsAsErrors = false) {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            androidTarget()

//            sourceSets.getByName("commonMain") {
//                dependencies {
//                    // add common dependencies
//                }
//            }

        }

        configureSpotless()
    }
}
