plugins {
    `android-library`
    `kotlin-multiplatform`
}

android {
    namespace = "app.shapeshifter.feature.onboarding.data"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                api(projects.data.datastore)

                implementation(libs.kotlininject.runtime)

                implementation(libs.androidx.datastore.preference.core)
            }
        }
    }
}
