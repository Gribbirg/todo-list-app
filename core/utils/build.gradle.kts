plugins {
    id("android-core-data-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.utils"
}

dependencies {
    api(project(":domain"))
}