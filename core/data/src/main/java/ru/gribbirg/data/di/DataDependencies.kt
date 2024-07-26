package ru.gribbirg.data.di

import android.content.Context
import io.ktor.client.engine.HttpClientEngine

interface DataDependencies {
    val context: Context
    val httpClient: HttpClientEngine
}