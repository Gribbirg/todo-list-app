plugins {
    id("android-core-data-convention")
    alias(libs.plugins.compose.compiler)
    id("kotlinx-serialization")
}

android {
    namespace = "ru.gribbirg.todoapp.network"
}

dependencies {
    api(project(":core:utils"))
    api(project(":domain"))
}