plugins {
    `android-library`
    `kotlin-multiplatform`
}

android {
    namespace = "app.shapeshifter.data.datastore"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(libs.androidx.datastore.core)
                implementation(libs.kotlininject.runtime)
            }
        }
    }
}
