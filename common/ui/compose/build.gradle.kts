import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
    alias(libs.plugins.kotlin.parcelize)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.data.models)
                implementation(compose.material3)
                implementation(libs.circuit.foundation)
                implementation(compose.components.resources)

                api(libs.coil.compose)
                implementation(libs.circuit.overlay)
                implementation(libs.circuit.runtime)
            }
        }
    }

    androidTarget {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=app.shapeshifter.common.ui.compose.screens.Parcelize",
            )
        }
    }
}

android {
    namespace = "app.shapeshifter.common.ui.compose"

    // tells android source set to include commonMain/resources
    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/res")
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "app.shapeshifter.common.ui.resources"
}
