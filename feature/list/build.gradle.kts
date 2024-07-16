plugins {
    id("android-feature-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.list"
}

dependencies {
    api(project(":domain"))
    api(project(":core:ui"))
    api(project(":core:data"))
    api(project(":core:utils"))
    api(project(":core:theme"))
}