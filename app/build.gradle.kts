plugins {
    `android-application`
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "app.shapeshifter"

    defaultConfig {
        applicationId = "app.shapeshifter"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

sqldelight {
    databases {
        create("ShapeShifterDatabase") {
            packageName.set("app.shapeshifter")
            dependency(projects.feature.home.ui)
        }
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

dependencies {
    implementation(projects.feature.home.ui)
    implementation(projects.core.base)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    implementation(libs.circuit.foundation)

    implementation(libs.kotlininject.runtime)
    ksp(libs.kotlininject.compiler)

    implementation(libs.sqldelight.androidDriver)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
