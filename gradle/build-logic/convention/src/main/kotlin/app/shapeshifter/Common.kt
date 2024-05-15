package app.shapeshifter

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets

fun Project.configureKotlin(
    enableWarningsAsErrors: Boolean = true,
    compilerOptions: KotlinCommonCompilerOptions.() -> Unit = {},
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
    action: KotlinCommonCompilerOptions.() -> Unit,
) {

    targets.forEach { target ->
        target.compilations.configureEach {
            compilerOptions.configure(action)
        }
    }
}

fun KotlinCommonCompilerOptions.setProjectDefaults(
    enableWarningsAsErrors: Boolean = true,
) {
    // Treat all Kotlin warnings as errors (disabled by default)
    allWarningsAsErrors.set(enableWarningsAsErrors)
}

fun KotlinCommonCompilerOptions.addCoroutinesCompilerArgs() {
    // Enable experimental coroutines APIs, including Flow
    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.FlowPreview")
    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
