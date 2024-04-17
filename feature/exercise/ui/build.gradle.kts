plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "app.shapeshifter.feature.exercise.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.common.ui.compose)
                implementation(projects.feature.exercise.data)

                implementation(libs.kotlininject.runtime)
                implementation(libs.circuit.foundation)
                implementation(compose.preview)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
    }
}
