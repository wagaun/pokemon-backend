package com.truelayer.pokemonbackend.client.httpclient

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.truelayer.pokemonbackend.client.FunTranslationsApiClient
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class JavaHttpClientFunTranslationsApiClient(private val type: Type,
                                             private val timeoutSeconds: Long,
                                             private val objectMapper: ObjectMapper) : FunTranslationsApiClient {
    enum class Type(val url: String) {
        YODA("https://api.funtranslations.com/translate/yoda.json"),
        SHAKESPEARE("https://api.funtranslations.com/translate/shakespeare.json"),
    }

    override fun translate(text: String): Result<FunTranslationsApiClient.Translated> {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
            .build()
        val requestBody = Request(text)
        val request = HttpRequest.newBuilder()
            .uri(URI.create(type.url))
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val statusCode = response.statusCode()
        return if (statusCode / 100 == 2) {
            val body = objectMapper.readValue(response.body(), Response::class.java)
            if (body.success.total == 1L) {
                Result.success(FunTranslationsApiClient.Translated(body.contents.translated))
            } else {
                Result.failure(Exception("Response failure from FunTranslations. Total Success value ${body.success.total}"))
            }
        } else {
            Result.failure(Exception("Error $statusCode calling FunTranslations"))
        }
    }

    data class Request(val text: String)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Response(@JsonProperty("success") val success: SuccessMetadata, @JsonProperty("contents") val contents: Contents)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SuccessMetadata(@JsonProperty("total") val total: Long)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Contents(@JsonProperty("translated") val translated: String)
}