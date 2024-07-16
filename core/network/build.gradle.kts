plugins {
    id("android-core-convention")
    alias(libs.plugins.compose.compiler)
    id("kotlinx-serialization") // TODO: libs
}

android {
    namespace = "ru.gribbirg.todoapp.network"
}

dependencies {
    api(project(":core:utils"))
    api(project(":domain"))
}