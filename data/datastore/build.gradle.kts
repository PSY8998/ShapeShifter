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
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preference)
                implementation(libs.kotlininject.runtime)
            }
        }
    }
}
