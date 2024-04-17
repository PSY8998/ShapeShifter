plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "app.shapeshifter.common.ui.compose"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material3)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
