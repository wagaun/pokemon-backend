package com.truelayer.pokemonbackend

import com.fasterxml.jackson.databind.ObjectMapper
import com.truelayer.pokemonbackend.client.FunTranslationsApiClient
import com.truelayer.pokemonbackend.client.httpclient.JavaHttpClientFunTranslationsApiClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class PokemonBackendApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<PokemonBackendApplication>(*args)
		}

		const val pokeApiTimeoutSeconds = "pokeApiTimeoutSeconds"
		const val yodaFunTranslationsApiClient = "yodaFunTranslationsApiClient"
		const val shakespeareFunTranslationsApiClient = "shakespeareFunTranslationsApiClient"
	}

	@Bean
	@Qualifier(pokeApiTimeoutSeconds)
	fun pokeApiTimeoutSeconds(): Long = 5L

	@Bean
	@Qualifier(yodaFunTranslationsApiClient)
	fun yodaFunTranslationsApiClient(objectMapper: ObjectMapper): FunTranslationsApiClient =
		JavaHttpClientFunTranslationsApiClient(type = JavaHttpClientFunTranslationsApiClient.Type.YODA,
			objectMapper = objectMapper,
			timeoutSeconds = 5L)

	@Bean
	@Qualifier(shakespeareFunTranslationsApiClient)
	fun shakespeareFunTranslationsApiClient(objectMapper: ObjectMapper): FunTranslationsApiClient =
		JavaHttpClientFunTranslationsApiClient(type = JavaHttpClientFunTranslationsApiClient.Type.SHAKESPEARE,
			objectMapper = objectMapper,
			timeoutSeconds = 5L)
}
