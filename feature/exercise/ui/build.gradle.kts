plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
}

android {
    namespace = "app.shapeshifter.feature.exercise.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.common.ui.compose)
            implementation(projects.feature.exercise.data)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)

            implementation(compose.components.resources)

            implementation(libs.kotlininject.runtime)

            implementation(libs.circuit.foundation)
            implementation(libs.circuit.retained)
        }
    }
}
