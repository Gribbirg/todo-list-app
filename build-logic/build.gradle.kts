plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

gradlePlugin {
    plugins.register("telegram-reporter") {
        id = "telegram-reporter"
        implementationClass = "ru.gribbirg.todoapp.plugins.TelegramReporterPlugin"
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.ksp.gradle)

    implementation(libs.kotlin.coroutines.core)

    implementation(libs.ktor.client)
    implementation(libs.ktor.client.okhttp)

    implementation(libs.zip.file)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}