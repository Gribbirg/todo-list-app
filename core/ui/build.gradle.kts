plugins {
    id("android-core-data-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.ui"
}

dependencies {
    //implementation(project(":core:utils"))
    api(project(":domain"))
}