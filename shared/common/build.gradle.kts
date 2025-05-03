plugins {
    `android-library`
    `kotlin-multiplatform`
    `compose-multiplatform`
    alias(libs.plugins.ksp)
}

android {
    namespace = "app.shapeshifter.shared.common"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.base)
                api(projects.common.ui.compose)
                api(projects.data.datastore)
                api(projects.data.db)
                api(projects.data.supabase)
                api(projects.data.datastore)
                api(projects.domain)
                api(projects.common.imageloading)
                api(projects.feature.root.ui)
                api(projects.feature.home.ui)
                api(projects.feature.exercise.ui)
                api(projects.feature.exercise.data)
                api(projects.feature.exercise.domain)
                api(projects.feature.workout.ui)
                api(projects.feature.workout.domain)
                api(projects.feature.profile.ui)
                api(projects.feature.onboarding.ui)
                api(projects.feature.onboarding.data)

                implementation(libs.kotlininject.runtime)
                implementation(libs.circuit.foundation)
            }
        }
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
