import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import java.io.File

lateinit var projectRootDir: File
    private set

internal fun setProjectRootDir(rootDir: File) {
    projectRootDir = rootDir
}

val Project.buildDirectory: File
    get() = layout.buildDirectory.asFile.get()

val Project.projectDirectory: File
    get() = layout.projectDirectory.asFile

/**
 * Retrieves the [libs][org.gradle.accessors.dm.LibrariesForLibs] extension.
 */
val Project.libs: LibrariesForLibs
    get() = (this as ExtensionAware).extensions.getByName("libs") as LibrariesForLibs

fun DependencyHandler.coreLibraryDesugaring(dependencyNotation: Any): Dependency? =
    add("coreLibraryDesugaring", dependencyNotation)

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun KotlinDependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    project.dependencies.add("testImplementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun KotlinDependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    project.dependencies.add("androidTestImplementation", dependencyNotation)

inline val PluginDependenciesSpec.`android-application`: PluginDependencySpec
    get() = id("shapeshifter.android.application")

inline val PluginDependenciesSpec.`android-library`: PluginDependencySpec
    get() = id("shapeshifter.android.library")

inline val PluginDependenciesSpec.`kotlin-library`: PluginDependencySpec
    get() = id("shapeshifter.kotlin.library")

inline val PluginDependenciesSpec.`kotlin-multiplatform`: PluginDependencySpec
    get() = id("shapeshifter.kotlin.multiplatform")

inline val PluginDependenciesSpec.`compose-multiplatform`: PluginDependencySpec
    get() = id("shapeshifter.compose.multiplatform")
