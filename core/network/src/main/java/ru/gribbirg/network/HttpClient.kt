package ru.gribbirg.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * HTTP client
 *
 * @see ItemsApiClientImpl
 */
internal fun getMainHttpClient(engine: HttpClientEngine) = HttpClient(engine) {
    expectSuccess = true
    followRedirects = false

    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
            useAlternativeNames = true
        })
    }

    install(HttpRequestRetry) {
        exponentialDelay(
            base = 1.1,
            maxDelayMs = 1000,
        )
        retryIf(3) { _, response ->
            response.status.let { !it.isSuccess() && it.value !in 400..499 }
        }
    }

    // TODO: Remove before release
    //install(Logging) {
    //    level = LogLevel.ALL
    //    logger = object : Logger {
    //        override fun log(message: String) {
    //            Log.v("internet", "Ktor: $message")
    //        }
    //    }
    //}
    //
    //install(ResponseObserver) {
    //    onResponse { response ->
    //        Log.d("internet", "HTTP status: ${response.status.value}")
    //    }
    //}

    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
    }
}