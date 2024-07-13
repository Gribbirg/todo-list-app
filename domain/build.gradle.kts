plugins {
    id("android-domain-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.gribbirg.todoapp.domain"
}
