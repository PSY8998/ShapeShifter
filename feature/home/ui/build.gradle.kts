plugins {
    `android-library`
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "app.shapeshifter.feature.home.ui"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

sqldelight {
    databases {
        create("ShapeShifterDatabase") {
            packageName.set("app.shapeshifter.feature.home.ui")
        }
    }
}

dependencies {
    implementation(projects.core.base)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.circuit.foundation)

    implementation(libs.kotlininject.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
