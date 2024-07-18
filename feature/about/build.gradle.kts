plugins {
    id("android-feature-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.about"
}

dependencies {
    api(project(":core:ui"))
    api(project(":core:theme"))
}