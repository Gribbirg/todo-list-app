import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
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

detekt {
    toolVersion = "1.23.6"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true

}

dependencies {
    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}