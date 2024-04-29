import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    `android-library`
    `kotlin-multiplatform`
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlin.serialization)
}

buildConfig {
    packageName("app.shapeshifter.data.supabase")

    buildConfigField(
        type = String::class.java,
        name = "SUPABASE_URL",
        value = provider { fromLocalProperties("SUPABASE_URL") },
    )

    buildConfigField(
        type = String::class.java,
        name = "SUPABASE_KEY",
        value = provider { fromLocalProperties("SUPABASE_KEY") },
    )
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.data.models)

            implementation(libs.kotlininject.runtime)
            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.supabase.database)
            implementation(libs.kotlinx.serialization)
        }

        androidMain.dependencies {
            implementation(libs.ktor.cio)
        }
    }
}

android {
    namespace = "app.shapeshifter.data.supabase"
}

fun fromLocalProperties(key: String): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty(key) ?: ""
}
