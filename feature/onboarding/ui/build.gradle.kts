plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
}

android {
    namespace = "app.shapeshifter.feature.onboarding.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                api(projects.common.ui.compose)

                implementation(projects.feature.onboarding.data)

                implementation(libs.circuit.foundation)

                implementation(libs.kotlininject.runtime)

                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.components.resources)
            }
        }
    }
}
dependencies {

}
