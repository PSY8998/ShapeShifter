package app.shapeshifter

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension

fun Project.configureKotlinMultiplatform(
    enableWarningsAsErrors: Boolean = true,
    compilerOptions: KotlinCommonCompilerOptions.() -> Unit,
) {
    kotlinMultiplatform {
        setProjectToolChainVersion()

        targets.configureEach {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        setProjectDefaults(enableWarningsAsErrors)
                        compilerOptions()
                    }
                }
            }
        }
    }
}

fun Project.configureKotlin(
    enableWarningsAsErrors: Boolean = true,
    compilerOptions: KotlinCommonCompilerOptions.() -> Unit = {},
) {

    kotlin {
        setProjectToolChainVersion()
        target.compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    setProjectDefaults(enableWarningsAsErrors)
                    compilerOptions()
                }
            }
        }
    }
}

fun Project.kotlinMultiplatform(action: KotlinMultiplatformExtension.() -> Unit) {
    val kotlinProjectExtension = extensions.findByType(KotlinMultiplatformExtension::class.java)

    if (kotlinProjectExtension != null) {
        action(kotlinProjectExtension)
    } else {
        throw IllegalStateException(
            "kotlin compiler options are only supported in android, jvm and multiplatform modules",
        )
    }
}

fun Project.kotlin(action: KotlinSingleTargetExtension<*>.() -> Unit) {
    val kotlinProjectExtension = extensions.findByType(KotlinSingleTargetExtension::class.java)

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
