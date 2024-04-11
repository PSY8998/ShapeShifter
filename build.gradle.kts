// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("shapeshifter.root")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.spotless) apply false
}
