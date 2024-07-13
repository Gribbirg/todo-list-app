plugins {
    id("android-core-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.db"
}

dependencies {
    api(project(":core:utils"))
    api(project(":domain"))
}