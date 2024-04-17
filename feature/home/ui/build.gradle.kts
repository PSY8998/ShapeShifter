plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "app.shapeshifter.feature.home.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.common.ui.compose)

                implementation(libs.circuit.foundation)

                implementation(libs.kotlininject.runtime)

                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.foundation)
            }
        }
    }
}
