package com.truelayer.pokemonbackend.client.httpclient

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.truelayer.pokemonbackend.PokemonBackendApplication
import com.truelayer.pokemonbackend.client.PokeApiClient
import com.truelayer.pokemonbackend.client.model.PokeApiClientPokemonSpecies
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration

/**
 * Implements PokeApiClient calling pokeapi.co pokemon-species API. It uses Java HttpClient to make Http calls.
 */
@Component
class JavaHttpClientPokeApiClient(@Qualifier(PokemonBackendApplication.pokeApiTimeoutSeconds) private val timeoutSeconds: Long,
                                  private val objectMapper: ObjectMapper) : PokeApiClient {

    companion object {
        const val DEFAULT_LANGUAGE_NAME = "en"
    }

    override fun getPokemonData(name: String): Result<PokeApiClientPokemonSpecies> {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://pokeapi.co/api/v2/pokemon-species/${name}"))
            .build()
        val response = client.send(request, BodyHandlers.ofString())
        val statusCode = response.statusCode()
        // 2XX
        return if (statusCode / 100 == 2) {
            val body = objectMapper.readValue(response.body(), ApiResponse::class.java)
            Result.success(PokeApiClientPokemonSpecies(name = body.name,
                flavorText = body.flavorTextEntries.find { entry -> DEFAULT_LANGUAGE_NAME == entry.language.name }?.flavorText,
                habitatName = body.habitat?.name,
                legendary = body.legendary))
        } else {
            // TODO - review this - how 3xx redirects will work?
            Result.failure(PokeApiException(response.statusCode(), response.body()))
        }
    }

    class PokeApiException(val httpStatusCode: Int, message: String) : Exception(message)

    /**
     * These data models are for PokeAPI representation.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ApiResponse(@JsonProperty("name") val name: String,
                           @JsonProperty("is_legendary") val legendary: Boolean?,
                           @JsonProperty("habitat") val habitat: Habitat?,
                           @JsonProperty("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Habitat(@JsonProperty("name") val name: String)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class FlavorTextEntry(@JsonProperty("flavor_text") val flavorText: String,
                                @JsonProperty("language") val language: Language)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Language(@JsonProperty("name") val name: String)
}
