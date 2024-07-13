plugins {
    id("android-core-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.data"
}

dependencies {
    api(project(":core:utils"))
    api(project(":core:network"))
    api(project(":core:db"))
    api(project(":domain"))
}