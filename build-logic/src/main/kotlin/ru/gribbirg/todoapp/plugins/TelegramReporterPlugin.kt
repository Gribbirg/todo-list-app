package ru.gribbirg.todoapp.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import ru.gribbirg.todoapp.api.TelegramApi
import ru.gribbirg.todoapp.tasks.SizeCheckTask
import ru.gribbirg.todoapp.tasks.TelegramReporterTask
import java.io.File
import java.util.Locale

class TelegramReporterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("Android not found")

        val extension = project.extensions.create("tgReporter", TelegramExtension::class)

        val telegramApi = TelegramApi(HttpClient(OkHttp))

        androidComponents.onVariants { variant ->

            val artifacts = variant.artifacts.get(SingleArtifact.APK)

            val checkSizeTask = project.tasks.register(
                "validateApkSizeFor${variant.name.getVariantName()}",
                SizeCheckTask::class.java,
                telegramApi
            )
            checkSizeTask.configure {
                apkDir.set(artifacts)
                token.set(extension.token)
                chatId.set(extension.chatId)
                maxSizeKb.set(extension.maxFileSizeKb.getOrElse(Int.MAX_VALUE))
                outputFile.set(
                    File(
                        "${project.layout.buildDirectory.get().asFile.absolutePath}/apk-size.txt"
                    )
                )
            }

            project.tasks.register(
                "reportTelegramApkFor${
                    variant
                        .name
                        .getVariantName()
                }",
                TelegramReporterTask::class.java,
                telegramApi
            ).configure {
                apkDir.set(artifacts)
                token.set(extension.token)
                chatId.set(extension.chatId)
                apkSizeFile.set(checkSizeTask.get().outputFile)
            }
        }
    }

    private fun String.getVariantName() =
        split("-")
            .joinToString("") { word ->
                word.replaceFirstChar { char ->
                    if (char.isLowerCase())
                        char.titlecase(Locale.getDefault())
                    else word
                }
            }
}

interface TelegramExtension {
    val chatId: Property<String>
    val token: Property<String>
    val maxFileSizeKb: Property<Int>
}

