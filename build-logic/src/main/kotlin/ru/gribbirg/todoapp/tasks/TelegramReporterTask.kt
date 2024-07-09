package ru.gribbirg.todoapp.tasks

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import okio.FileNotFoundException
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import ru.gribbirg.todoapp.api.TelegramApi
import javax.inject.Inject

abstract class TelegramReporterTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:InputFile
    abstract val apkSizeFile: RegularFileProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun report() {
        val token = token.get()
        val chatId = chatId.get()
        apkDir.get().asFile.listFiles()
            ?.firstOrNull { it.name.endsWith(".apk") }
            ?.let {
                runBlocking {
                    telegramApi.sendMessage(
                        "Build finished!\nApk size: ${apkSizeFile.get().asFile.readText()}kb",
                        token,
                        chatId
                    ).apply {
                        println(bodyAsText())
                    }
                }
                runBlocking {
                    telegramApi.upload(it, token, chatId).apply {
                        println(bodyAsText())
                    }
                }
            } ?: throw FileNotFoundException("Apk not found")
    }
}