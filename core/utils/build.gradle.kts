plugins {
    id("android-core-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.utils"
}

dependencies {
    api(project(":domain"))
}