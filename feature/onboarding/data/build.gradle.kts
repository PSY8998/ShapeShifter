plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
}

android {
    namespace = "app.shapeshifter.feature.onboarding.data"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                api(projects.common.ui.compose)
                api(projects.data.datastore)

                implementation(libs.circuit.foundation)

                implementation(libs.kotlininject.runtime)

                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.components.resources)

                implementation(libs.androidx.datastore.preference.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.datastore.preference.android)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.androidx.datastore.preference.jvm)
            }
        }
    }
}
