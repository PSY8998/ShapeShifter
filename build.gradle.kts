// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("shapeshifter.root")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.spotless) apply false
}

buildscript {
    dependencies {
        // Yuck. Need to force kotlinpoet:1.16.0 as that is what buildconfig uses.
        // CMP 1.6.0-x uses kotlinpoet:1.14.x. Gradle seems to force 1.14.x which then breaks
        // buildconfig.
        classpath("com.squareup:kotlinpoet:1.16.0")
    }
}
