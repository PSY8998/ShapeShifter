plugins {
    `kotlin-library`
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("ShapeShifterDatabase") {
            packageName.set("app.shapeshifter.feature.exercise.data")
        }
    }
}

dependencies {
    implementation(libs.kotlininject.runtime)
}
