package ru.gribbirg.todoapp.utils

import okio.FileNotFoundException
import org.gradle.api.file.Directory

internal fun Directory.findApk() =
    asFile
        .listFiles()
        ?.firstOrNull { it.name.endsWith(".apk") }
        ?: throw FileNotFoundException("Apk file not found in ${asFile.absolutePath}")