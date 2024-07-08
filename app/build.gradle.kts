import java.util.Properties

plugins {
    alias(libs.plugins.compose.compiler)
    id("android-app-convention")
    id("telegram-reporter")
}

tgReporter {
    val properties = Properties()
    properties.load(project.rootProject.file("secrets.properties").inputStream())
    token = properties.getProperty("TELEGRAM_BOT_API")
    chatId = properties.getProperty("TELEGRAM_CHAT_ID")
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