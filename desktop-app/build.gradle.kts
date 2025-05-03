import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.compose.reload.core.HotReloadEnvironment.mainClass

// Copyright 2023, Google LLC, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

plugins {
    `kotlin-multiplatform`
    `compose-multiplatform`
    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha09"
}

kotlin {
    sourceSets {
        jvmMain {
            dependencies {
                implementation(projects.shared.prod)
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlin.coroutines.swing)

                implementation(libs.circuit.foundation)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "app.shapeshifter.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "app.shapehifter"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass.set("app.shapeshifter.MainKt")
}
