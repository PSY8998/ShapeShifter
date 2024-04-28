plugins {
    `kotlin-multiplatform`
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlininject.runtime)
        }
    }
}
