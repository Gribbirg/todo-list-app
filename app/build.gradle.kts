plugins {
    alias(libs.plugins.compose.compiler)
    id("android-app-convention")
}

android {
    defaultConfig {
        applicationId = "ru.gribbirg.todoapp"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    testImplementation(libs.junit)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}