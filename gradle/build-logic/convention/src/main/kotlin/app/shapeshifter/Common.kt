package app.shapeshifter

import buildDirectory
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import projectDirectory

fun Project.configureKotlin(
    enableWarningsAsErrors: Boolean = true,
    compilerOptions: KotlinJvmCompilerOptions.() -> Unit = {},
) {
    kotlin {
        setProjectToolChainVersion()

        compilerOptions {
            compilerOptions()
            setProjectDefaults(enableWarningsAsErrors)
        }
    }
}

fun Project.kotlin(action: KotlinProjectExtension.() -> Unit) {
    val kotlinProjectExtension = extensions.findByType(KotlinProjectExtension::class.java)

    if (kotlinProjectExtension != null) {
        action(kotlinProjectExtension)
    } else {
        throw IllegalStateException(
            "kotlin compiler options are only supported in android, jvm and multiplatform modules",
        )
    }
}

fun KotlinProjectExtension.setProjectToolChainVersion() {
    jvmToolchain(17)
}

fun KotlinProjectExtension.compilerOptions(
    action: KotlinJvmCompilerOptions.() -> Unit,
) = project(
    androidProject = {
        compilerOptions(action)
    },
    kotlinJvmProject = {
        compilerOptions(action)
    },
    kotlinMultiplatformProject = {
        androidTarget {
            compilations.configureEach {
                compilerOptions.configure(action)
            }
        }
    },
)

fun KotlinProjectExtension.project(
    androidProject: KotlinAndroidProjectExtension.() -> Unit,
    kotlinJvmProject: KotlinJvmProjectExtension.() -> Unit,
    kotlinMultiplatformProject: KotlinMultiplatformExtension.() -> Unit,
) {
    when (this) {
        is KotlinAndroidProjectExtension -> androidProject()
        is KotlinJvmProjectExtension -> kotlinJvmProject()
        is KotlinMultiplatformExtension -> kotlinMultiplatformProject()
    }
}

fun KotlinJvmCompilerOptions.setProjectDefaults(
    enableWarningsAsErrors: Boolean = true,
) {
    // Treat all Kotlin warnings as errors (disabled by default)
    allWarningsAsErrors.set(enableWarningsAsErrors)
    addK2CompilerArgs()
}

/**
 * Make sure to remove this with kotlin 2.0.0
 * K2 compiler will be default
 */
fun KotlinJvmCompilerOptions.addK2CompilerArgs() {
    // Suppress compileKotlin's warning:
    // > Language version 2.0 is experimental, there are no backwards compatibility guarantees for new language and library features
    freeCompilerArgs.add("-Xsuppress-version-warnings")
}

fun KotlinJvmCompilerOptions.addCoroutinesCompilerArgs() {
    // Enable experimental coroutines APIs, including Flow
    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.FlowPreview")
    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}

/**
 * https://chris.banes.dev/posts/composable-metrics/
 */
fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers
        .gradleProperty("enableComposeCompilerMetrics")
    val enableMetrics = (enableMetricsProvider.orNull == "true")

    if (enableMetrics) {
        val metricsFolder = "${project.buildDirectory}/compose-metrics"
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$metricsFolder",
        )

        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$metricsFolder",
        )

        val isMetricsTaskCacheable = plugins.hasPlugin("com.android.application")
        tasks.withType<KotlinCompile> {
            outputs.upToDateWhen { isMetricsTaskCacheable }
        }
    }

    return metricParameters.toList()
}

fun Project.buildComposeStabilityConfigurationParameters(): List<String> {
    val configParameters = mutableListOf<String>()
    val configFolder = "${project.projectDirectory.absolutePath}/compose_compiler_config.conf"

    configParameters.add("-P")
    configParameters.add(
        "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=$configFolder",
    )
    return configParameters.toList()
}
