package ru.gribbirg.todoapp.tasks

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.gribbirg.todoapp.api.TelegramApi
import ru.gribbirg.todoapp.utils.findApk
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
    abstract val maxSizeKb: Property<Int>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun check() {
        val maxSizeKb = maxSizeKb.get()
        val token = token.get()
        val chatId = chatId.get()

        val file = apkDir.get().findApk()

        val size = file.length() / 1024
        if (size > maxSizeKb) {
            val text =
                "Apk is too large!\nName: ${file.name}\nSize: $size KB\nMax size: $maxSizeKb KB"
            runBlocking {
                telegramApi.sendMessage(
                    text,
                    token,
                    chatId,
                ).apply {
                    println(bodyAsText())
                }
            }
            throw Exception(text)
        } else {
            outputFile.get().asFile.writeText(size.toString())
        }
    }
}