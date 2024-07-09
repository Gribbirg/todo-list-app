package ru.gribbirg.todoapp.tasks

import kotlinx.coroutines.runBlocking
import okio.FileNotFoundException
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.gribbirg.todoapp.api.TelegramApi
import javax.inject.Inject

abstract class SizeCheckTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:Input
    abstract val maxSizeKb: Property<Int?>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun check() {
        val maxSizeKb = maxSizeKb.get() ?: return
        val token = token.get()
        val chatId = chatId.get()

        val file = apkDir
            .get()
            .asFile
            .listFiles()
            ?.firstOrNull { it.name.endsWith(".apk") }

        if (file == null) {
            throw FileNotFoundException("Apk not found")
        }

        val size = file.length() / 1024
        if (size > maxSizeKb) {
            val text =
                "Apk is too large!\nName: ${file.name}\nSize: ${size}kb\nMax size: ${maxSizeKb}kb"
            runBlocking {
                telegramApi.sendMessage(
                    text,
                    token,
                    chatId,
                )
            }
            throw Exception(text)
        } else {
            outputFile.get().asFile.writeText(size.toString())
        }
    }
}