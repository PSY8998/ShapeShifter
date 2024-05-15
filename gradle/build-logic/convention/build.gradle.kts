plugins {
    `kotlin-dsl`
}

group = "app.shapeshifter.convention.build-logic"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    // Workaround for accessing libs
    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("root") {
            id = "shapeshifter.root"
            implementationClass = "RootConventionPlugin"
        }

        register("androidApplication") {
            id = "shapeshifter.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "shapeshifter.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("kotlinLibrary") {
            id = "shapeshifter.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }

        register("kotlinMultiplatform") {
            id = "shapeshifter.kotlin.multiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }

        register("composeMultiplatform") {
            id = "shapeshifter.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
    }
}
