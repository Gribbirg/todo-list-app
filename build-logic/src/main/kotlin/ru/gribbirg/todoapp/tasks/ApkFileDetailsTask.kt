package ru.gribbirg.todoapp.tasks

import kotlinx.coroutines.runBlocking
import net.lingala.zip4j.ZipFile
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import ru.gribbirg.todoapp.api.TelegramApi
import ru.gribbirg.todoapp.utils.findApk
import ru.gribbirg.todoapp.utils.roundTo
import javax.inject.Inject

abstract class ApkFileDetailsTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:Input
    abstract val enabledTask: Property<Boolean>

    @TaskAction
    fun sendDetails() {
        if (!enabledTask.get()) return

        val file = apkDir.get().findApk()

        val text = ZipFile(file)
            .fileHeaders
            .groupBy { it.fileName.split("/").first() }
            .map { it.key to it.value.sumOf { header -> header.uncompressedSize } / 1024.0 }
            .joinToString("\n") {
                "${it.first} - ${it.second.roundTo(2)} KB"
            }

        runBlocking {
            telegramApi.sendMessage(
                "Apk details:\n\n$text",
                token.get(),
                chatId.get()
            )
        }
    }
}