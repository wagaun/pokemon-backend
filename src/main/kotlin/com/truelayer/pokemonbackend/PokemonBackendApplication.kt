package com.truelayer.pokemonbackend

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
	}

	@Bean
	@Qualifier("pokeApiTimeoutSeconds")
	fun pokeApiTimeoutSeconds(): Long = 5L

}
