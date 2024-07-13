plugins {
    id("android-core-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.ui"
}

dependencies {
    api(project(":domain"))
}