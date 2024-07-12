plugins {
    id("android-core-data-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.edit"
}

dependencies {
    api(project(":domain"))
    api(project(":core:ui"))
    api(project(":core:data"))
}