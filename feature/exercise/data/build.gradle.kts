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
                implementation(projects.data.db)

                implementation(libs.kotlininject.runtime)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.sqldelight.coroutines)
            }
        }
    }
}


android {
    namespace = "app.shapeshifter.feature.exercise.data"
}
