package com.truelayer.pokemonbackend.controller

import com.truelayer.pokemonbackend.client.FunTranslationsApiClient
import com.truelayer.pokemonbackend.client.PokeApiClient
import com.truelayer.pokemonbackend.client.httpclient.JavaHttpClientPokeApiClient
import com.truelayer.pokemonbackend.client.model.PokeApiClientPokemonSpecies
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.assertFailsWith

internal class PokemonRestControllerTest {

    @MockK lateinit var pokeApiClient: PokeApiClient
    @MockK lateinit var yodaFunTranslationsApiClient: FunTranslationsApiClient
    @MockK lateinit var shakespeareFunTranslationsApiClient: FunTranslationsApiClient
    private lateinit var controller: PokemonRestController

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        controller = PokemonRestController(
            pokeApiClient = pokeApiClient,
            yodaFunTranslationsApiClient = yodaFunTranslationsApiClient,
            shakespeareFunTranslationsApiClient = shakespeareFunTranslationsApiClient)
    }

    @Test
    fun `Test pokemon method`() {
        // Arrange
        val pokemonName = "Boris"
        val habitat = "London"
        val flavorText = "I'm Boris the British Pokemon"
        val clientOutput =  PokeApiClientPokemonSpecies(
            name = pokemonName,
            legendary = false,
            habitatName = habitat,
            flavorText = flavorText,
        )
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.success(clientOutput)

        // Act
        val output = controller.pokemon(pokemonName)

        // Assert
        assertThat(output.name, equalTo(pokemonName))
        assertThat(output.legendary, equalTo(false))
        assertThat(output.habitat, equalTo(habitat))
        assertThat(output.description, equalTo(flavorText))
    }

    @Test
    fun `Test pokemon method error handling`() {
        // Arrange
        val pokemonName = "Boris"
        val clientException = RuntimeException()
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.failure(clientException)

        // Act
        val controllerException = assertFailsWith<RuntimeException>{
            controller.pokemon(pokemonName)
        }

        // Assert
        assertThat(controllerException, equalTo(clientException))
    }

    @Test
    fun `Test pokemon not found`() {
        // Arrange
        val pokemonName = "Boris"
        val clientException = JavaHttpClientPokeApiClient.PokeApiException(httpStatusCode = 404,
            message = "Client 404 exception")
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.failure(clientException)

        // Act
        val controllerException = assertFailsWith<ResponseStatusException>{
            controller.pokemon(pokemonName)
        }

        // Assert
        assertThat(controllerException.status, equalTo(HttpStatus.NOT_FOUND))
        assertThat(controllerException.reason, equalTo("Pokemon not found"))
    }

    @Test
    fun `Test pokemon non 404 client error`() {
        // Arrange
        val pokemonName = "Boris"
        val clientException = JavaHttpClientPokeApiClient.PokeApiException(httpStatusCode = 500,
            message = "Client 500 exception")
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.failure(clientException)

        // Act
        val controllerException = assertFailsWith<JavaHttpClientPokeApiClient.PokeApiException>{
            controller.pokemon(pokemonName)
        }

        // Assert
        assertThat(controllerException, equalTo(clientException))
    }

    @Test
    fun `Test translatedPokemon method`() {
        // Arrange
        val pokemonName = "Boris"
        val habitat = "London"
        val flavorText = "I'm Boris the British Pokemon"
        val clientOutput =  PokeApiClientPokemonSpecies(
            name = pokemonName,
            legendary = false,
            habitatName = habitat,
            flavorText = flavorText,
        )
        val translated = "British Pokemon Boris I am"
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.success(clientOutput)
        every { shakespeareFunTranslationsApiClient.translate(flavorText) } returns Result.success(FunTranslationsApiClient.Translated(translated))

        // Act
        val output = controller.translatedPokemon(pokemonName)

        // Assert
        assertThat(output.name, equalTo(pokemonName))
        assertThat(output.description, equalTo(translated))
        assertThat(output.habitat, equalTo(habitat))
        assertThat(output.legendary, equalTo(false))
    }

    @Test
    fun `Test translatedPokemon method fallback`() {
        // Arrange
        val pokemonName = "Boris"
        val habitat = "London"
        val flavorText = "I'm Boris the British Pokemon"
        val clientOutput =  PokeApiClientPokemonSpecies(
            name = pokemonName,
            legendary = false,
            habitatName = habitat,
            flavorText = flavorText,
        )
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.success(clientOutput)
        every { shakespeareFunTranslationsApiClient.translate(flavorText) } returns Result.failure(RuntimeException())

        // Act
        val output = controller.translatedPokemon(pokemonName)

        // Assert
        assertThat(output.name, equalTo(pokemonName))
        assertThat(output.description, equalTo(flavorText))
        assertThat(output.habitat, equalTo(habitat))
        assertThat(output.legendary, equalTo(false))
    }

    @Test
    fun `Test legendary translatedPokemon method`() {
        // Arrange
        val pokemonName = "LegendJerry"
        val habitat = "London"
        val flavorText = "I'm not sure"
        val clientOutput =  PokeApiClientPokemonSpecies(
            name = pokemonName,
            legendary = true,
            habitatName = habitat,
            flavorText = flavorText,
        )
        val translated = "To be or not to be"
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.success(clientOutput)
        every { yodaFunTranslationsApiClient.translate(flavorText) } returns Result.success(FunTranslationsApiClient.Translated(translated))

        // Act
        val output = controller.translatedPokemon(pokemonName)

        // Assert
        assertThat(output.name, equalTo(pokemonName))
        assertThat(output.description, equalTo(translated))
        assertThat(output.habitat, equalTo(habitat))
        assertThat(output.legendary, equalTo(true))
    }

    @Test
    fun `Test cave translatedPokemon method`() {
        // Arrange
        val pokemonName = "LegendJerry"
        val habitat = "cave"
        val flavorText = "I'm not sure"
        val clientOutput =  PokeApiClientPokemonSpecies(
            name = pokemonName,
            legendary = false,
            habitatName = habitat,
            flavorText = flavorText,
        )
        val translated = "To be or not to be"
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.success(clientOutput)
        every { yodaFunTranslationsApiClient.translate(flavorText) } returns Result.success(FunTranslationsApiClient.Translated(translated))

        // Act
        val output = controller.translatedPokemon(pokemonName)

        // Assert
        assertThat(output.name, equalTo(pokemonName))
        assertThat(output.description, equalTo(translated))
        assertThat(output.habitat, equalTo(habitat))
        assertThat(output.legendary, equalTo(false))
    }

    @Test
    fun `Test cave translatedPokemon method fallback`() {
        // Arrange
        val pokemonName = "LegendJerry"
        val habitat = "cave"
        val flavorText = "I'm not sure"
        val clientOutput =  PokeApiClientPokemonSpecies(
            name = pokemonName,
            legendary = false,
            habitatName = habitat,
            flavorText = flavorText,
        )
        every { pokeApiClient.getPokemonData(pokemonName) } returns Result.success(clientOutput)
        every { yodaFunTranslationsApiClient.translate(flavorText) } returns Result.failure(RuntimeException())

        // Act
        val output = controller.translatedPokemon(pokemonName)

        // Assert
        assertThat(output.name, equalTo(pokemonName))
        assertThat(output.description, equalTo(flavorText))
        assertThat(output.habitat, equalTo(habitat))
        assertThat(output.legendary, equalTo(false))
    }
}