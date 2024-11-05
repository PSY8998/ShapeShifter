plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
}

android {
    namespace = "app.shapeshifter.feature.profile.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                api(projects.common.ui.compose)

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
    implementation(project(":data:models"))
    implementation(project(":feature:workout:domain"))
    implementation(project(":feature:workout:ui"))
}
