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
                implementation(projects.feature.exercise.data)

                implementation(libs.kotlininject.runtime)
            }
        }
    }
}

android {
    namespace = "app.shapeshifter.feature.exercise.domain"
}
