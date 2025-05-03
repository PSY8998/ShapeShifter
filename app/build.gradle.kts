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
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf(
            // Exclude AndroidX version files
            "META-INF/*.version",
            // Exclude consumer proguard files
            "META-INF/proguard/*",
            // Exclude the Firebase/Fabric/other random properties files
            "/*.properties",
            "fabric/*.properties",
            "META-INF/*.properties",
            // License files
            "LICENSE*",
            // Exclude Kotlin unused files
            "META-INF/**/previous-compilation-data.bin",
        )
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

dependencies {
    implementation(projects.shared.prod)
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
    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.splashscreen)
}
