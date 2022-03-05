package com.truelayer.pokemonbackend.client.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class JavaHttpClientFunTranslationsApiClientTest {
    private val objectMapper = ObjectMapper()

    @Test
    @Disabled("This is not an unit test as it does real http calls, consider to make it an integration test in the future if there is no risk of being rate limited")
    fun `test Shakespeare translation`() {
        val client = JavaHttpClientFunTranslationsApiClient(timeoutSeconds = 5,
            type = JavaHttpClientFunTranslationsApiClient.Type.SHAKESPEARE,
            objectMapper = objectMapper
        )
        val output = client.translate("Master Obiwan has lost a planet.")
        assertThat(output.isSuccess, equalTo(true))
        assertThat(output.getOrNull()?.translatedText, `is`(notNullValue()))
    }

    @Test
    @Disabled("This is not an unit test as it does real http calls, consider to make it an integration test in the future if there is no risk of being rate limited")
    fun `test Yoda translation`() {
        val client = JavaHttpClientFunTranslationsApiClient(timeoutSeconds = 5,
            type = JavaHttpClientFunTranslationsApiClient.Type.YODA,
            objectMapper = objectMapper
        )
        val output = client.translate("Master Obiwan has lost a planet.")
        assertThat(output.isSuccess, equalTo(true))
        assertThat(output.getOrNull()?.translatedText, `is`(notNullValue()))
    }
}