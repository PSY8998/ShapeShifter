plugins {
    `android-library`
    `kotlin-multiplatform`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.data.models)
                api(projects.domain)
                implementation(projects.data.db)

                implementation(libs.kotlininject.runtime)

            }
        }
    }
}

android {
    namespace = "app.shapeshifter.feature.workout.domain"
}
