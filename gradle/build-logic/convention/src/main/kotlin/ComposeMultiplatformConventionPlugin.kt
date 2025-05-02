import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        configureCompose()
    }
}

fun Project.configureCompose() {
    composeCompiler {
        // Needed for Layout Inspector to be able to see all of the nodes in the component tree:
        //https://issuetracker.google.com/issues/338842143
        includeSourceInformation.set(true)

        if (project.providers.gradleProperty("enableComposeCompilerReports").isPresent) {
            val composeReports = layout.buildDirectory.map { it.dir("reports").dir("compose") }
            reportsDestination.set(composeReports)
            metricsDestination.set(composeReports)
        }

        stabilityConfigurationFiles.addAll(
            RegularFile { rootProject.file("compose-stability.conf") },
        )
    }
}

fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
    extensions.configure<ComposeCompilerGradlePluginExtension>(block)
}
