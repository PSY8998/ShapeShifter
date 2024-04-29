plugins {
    `kotlin-library`
}

dependencies {
    implementation(libs.kotlinx.atomicfu)
    implementation(libs.kotlin.coroutines.core)

    api(libs.paging.common)
}
