package app.shapeshifter

import buildDirectory
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.configureSpotless() {
    val spotlessDependency = libs.plugins.spotless.get()
    pluginManager.apply(spotlessDependency.pluginId)

    val ktlintVersion = libs.versions.ktlint.get()

    pluginManager.apply(spotlessDependency.pluginId)

    spotless {
        // https://github.com/diffplug/spotless/issues/1644
        lineEndings = LineEnding.UNIX // or any other except GIT_ATTRIBUTES

        kotlin {
            target("**/*.kt")
            targetExclude("$buildDirectory/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint(ktlintVersion)
                .setEditorConfigPath("$rootDir/.editorconfig")
        }
        kotlinGradle {
            target("**/*.kts")
            targetExclude("$buildDirectory/**/*.kts")
            ktlint(ktlintVersion)
                .setEditorConfigPath("$rootDir/.editorconfig")
        }
    }
}

private fun Project.spotless(action: SpotlessExtension.() -> Unit) =
    extensions.configure(action)
