import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
    alias(libs.plugins.ksp)
}

android {
    namespace = "app.shapeshifter.shared.prod"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.common)

                implementation(libs.kotlininject.runtime)
                implementation(libs.circuit.foundation)
            }
        }

        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.framework {
                isStatic = true
                baseName = "ShapeshifterKt"

                export(projects.feature.root)
            }
        }
    }
}

android {
    namespace = "app.shapeshifter.shared.prod"
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
