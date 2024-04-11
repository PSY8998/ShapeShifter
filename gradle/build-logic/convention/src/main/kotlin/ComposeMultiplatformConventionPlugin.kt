import app.shapeshifter.buildComposeMetricsParameters
import app.shapeshifter.buildComposeStabilityConfigurationParameters
import app.shapeshifter.compilerOptions
import app.shapeshifter.kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        configureCompose()
    }

}

fun Project.configureCompose() {
    compose {
        // The Compose Compiler for 1.9.22 works for 1.9.23
        kotlinCompilerPlugin.set(dependencies.compiler.forKotlin(libs.versions.kotlin.get()))

        kotlinCompilerPluginArgs.addAll(
            // Enable 'strong skipping'
            // https://medium.com/androiddevelopers/jetpack-compose-strong-skipping-mode-explained-cbdb2aa4b900
            "experimentalStrongSkipping=true",
        )
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll(buildComposeStabilityConfigurationParameters())
            freeCompilerArgs.addAll(buildComposeMetricsParameters())
        }
    }
}

fun Project.compose(block: ComposeExtension.() -> Unit) {
    extensions.configure<ComposeExtension>(block)
}


