plugins {
    `android-library`
    `kotlin-multiplatform`
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coil.core)
            implementation(libs.coil.network)

            implementation(libs.kotlininject.runtime)
        }
    }
}

android {
    namespace = "app.shapeshifter.common.imageloading"
}
