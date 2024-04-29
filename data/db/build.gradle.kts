plugins {
    `android-library`
    `kotlin-multiplatform`
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("ShapeShifterDatabase") {
            packageName.set("app.shapeshifter.data.db")
        }
    }
}

android {
    namespace = "app.shapeshifter.data.db"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.data.models)
            implementation(libs.kotlininject.runtime)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.sqldelight.coroutines)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.androidDriver)
        }
    }
}
