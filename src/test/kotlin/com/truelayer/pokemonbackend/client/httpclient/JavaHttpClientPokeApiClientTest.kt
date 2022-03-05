package com.truelayer.pokemonbackend.client.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class JavaHttpClientPokeApiClientTest {
    private val client = JavaHttpClientPokeApiClient(timeoutSeconds = 5, ObjectMapper())

    @Test
    @Disabled("This is not an unit test as it does real http calls, consider to make it an integration test in the future")
    fun successfulCallTest() {
        val output = client.getPokemonData("mewtwo")
        assertThat(output.isSuccess, equalTo(true))
        val outputModel = output.getOrThrow()
        assertThat(outputModel.name, equalTo("mewtwo"))
        assertThat(outputModel.flavorText, `is`(notNullValue()))
        assertThat(outputModel.habitatName, equalTo("rare"))
        assertThat(outputModel.legendary, equalTo(true))
    }

    @Test
    @Disabled("This is not an unit test as it does real http calls, consider to make it an integration test in the future")
    fun handleNotFound() {
        val output = client.getPokemonData("potato")
        assertThat(output.isFailure, equalTo(true))
        val exception = output.exceptionOrNull() as JavaHttpClientPokeApiClient.PokeApiException
        assertThat(exception, isA(JavaHttpClientPokeApiClient.PokeApiException::class.java))
        assertThat(exception.httpStatusCode, equalTo(404))
        assertThat(exception.message, `is`(notNullValue()))
    }
}