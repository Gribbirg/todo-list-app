plugins {
    id("android-core-convention")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.gribbirg.todoapp.utils"
}

dependencies {
    api(project(":domain"))
}