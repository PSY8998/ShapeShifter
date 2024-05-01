plugins {
    `android-application`
    `compose-multiplatform`
    alias(libs.plugins.ksp)
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

    buildFeatures {
        buildConfig = true
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

dependencies {
    // core
    implementation(projects.core.base)
    implementation(projects.common.ui.compose)
    implementation(projects.data.db)
    implementation(projects.data.supabase)
    implementation(projects.domain)

    implementation(projects.common.imageloading)

    // features
    implementation(projects.feature.root.ui)

    // home
    implementation(projects.feature.home.ui)

    // exercise
    implementation(projects.feature.exercise.ui)
    implementation(projects.feature.exercise.data)
    implementation(projects.feature.exercise.domain)

    // workout
    implementation(projects.feature.workout.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    implementation(libs.circuit.foundation)

    implementation(libs.kotlininject.runtime)
    ksp(libs.kotlininject.compiler)

    implementation(project.dependencies.platform(libs.supabase.bom))
    implementation(libs.supabase.database)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
