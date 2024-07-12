plugins {
    id("android-core-data-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.db"
}

dependencies {
    api(project(":core:utils"))
    api(project(":domain"))
}